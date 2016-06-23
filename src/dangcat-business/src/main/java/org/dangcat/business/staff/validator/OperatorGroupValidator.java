package org.dangcat.business.staff.validator;

import org.dangcat.business.staff.domain.OperatorGroup;
import org.dangcat.business.staff.domain.OperatorInfo;
import org.dangcat.business.staff.exceptions.OperatorGroupException;
import org.dangcat.business.staff.service.impl.OperatorGroupLoader;
import org.dangcat.business.validator.BusinessValidator;
import org.dangcat.commons.utils.ValueUtils;
import org.dangcat.framework.exception.ServiceException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

public class OperatorGroupValidator extends BusinessValidator<OperatorGroup>
{
    @Override
    public void beforeDelete(OperatorGroup operatorGroup) throws ServiceException
    {
        // �Ѿ��󶨲���Ա���鲻��ɾ����
        if (this.exists(OperatorInfo.class, OperatorInfo.GroupId, operatorGroup.getId()))
            throw new OperatorGroupException(OperatorGroupException.CHILE_OPERATOR_EXISTS);
        // ����ɾ��������Ա�顣
        if (this.isParent(operatorGroup.getId()))
            throw new OperatorGroupException(OperatorGroupException.DELETE_DENY_FOR_ISPARENT);
        // ֻ��ɾ���ӳ�Ա�顣
        if (!this.isMembers(operatorGroup.getId()))
            throw new OperatorGroupException(OperatorGroupException.DELETE_DENY_FOR_NOTMEMBER);
    }

    @Override
    public void beforeSave(OperatorGroup operatorGroup) throws ServiceException
    {
        this.validateExists(operatorGroup);
        if (operatorGroup.hasError())
            return;

        this.validateMember(operatorGroup);
        if (operatorGroup.hasError())
            return;

        this.validateName(operatorGroup);
        this.validateParent(operatorGroup);
    }

    private boolean isMembers(Integer groupId)
    {
        OperatorGroupLoader operatorGroupLoader = new OperatorGroupLoader(this.getEntityManager());
        Map<Integer, String> operatorGroupMap = operatorGroupLoader.loadMembers(null);
        if (operatorGroupMap != null)
            return operatorGroupMap.containsKey(groupId);
        return true;
    }

    private boolean isParent(Integer groupId)
    {
        OperatorGroupLoader operatorGroupLoader = new OperatorGroupLoader(this.getEntityManager());
        Map<Integer, String> operatorGroupMap = operatorGroupLoader.loadMembers(groupId);
        if (operatorGroupMap != null)
            return operatorGroupMap.size() > 1;
        return false;
    }

    private void validateExists(OperatorGroup operatorGroup)
    {
        if (operatorGroup.getId() != null)
        {
            // ����Ա�鲻���ڡ�
            if (!this.exists(OperatorGroup.class, OperatorGroup.Id, operatorGroup.getId()))
                operatorGroup.addServiceException(new OperatorGroupException(OperatorGroup.Name, OperatorGroupException.DATA_NOTEXISTS));
        }
    }

    private void validateMember(OperatorGroup operatorGroup) throws OperatorGroupException
    {
        if (operatorGroup.getId() != null)
        {
            if (!this.isMembers(operatorGroup.getId()))
                operatorGroup.addServiceException(new OperatorGroupException(OperatorGroupException.MODIFY_DENY_FOR_NOTMEMBER));
        }
    }

    private void validateName(OperatorGroup operatorGroup)
    {
        if (!ValueUtils.isEmpty(operatorGroup.getName()))
        {
            // ����Ա������Ʋ����ظ���
            if (this.checkRepeat(OperatorGroup.class, operatorGroup, OperatorGroup.Name, operatorGroup.getName()))
                operatorGroup.addServiceException(new OperatorGroupException(OperatorGroup.Name, OperatorGroupException.DATA_REPEAT));
        }
    }

    private void validateParent(OperatorGroup operatorGroup)
    {
        if (operatorGroup.getParentId() != null)
        {
            // ��������Ա�鲻���ڡ�
            OperatorGroup parentGroup = this.getEntityManager().load(OperatorGroup.class, operatorGroup.getParentId());
            if (parentGroup == null)
                operatorGroup.addServiceException(new OperatorGroupException(OperatorGroup.ParentId, OperatorGroupException.DATA_NOTEXISTS));
            else if (operatorGroup.getId() != null)
            {
                // ��������Ա�鲻��ѭ���󶨡�
                Collection<OperatorGroup> operatorGroupCollection = new HashSet<OperatorGroup>();
                while (parentGroup != null)
                {
                    if (operatorGroupCollection.contains(parentGroup))
                        break;

                    if (operatorGroup.getId().equals(parentGroup.getId()))
                    {
                        operatorGroup.addServiceException(new OperatorGroupException(OperatorGroup.ParentId, OperatorGroupException.BIND_CYCLING));
                        break;
                    }

                    operatorGroupCollection.add(parentGroup);
                    if (parentGroup.getParentId() != null)
                        parentGroup = this.getEntityManager().load(OperatorGroup.class, parentGroup.getParentId());
                    else
                        parentGroup = null;
                }
            }
        }
    }
}

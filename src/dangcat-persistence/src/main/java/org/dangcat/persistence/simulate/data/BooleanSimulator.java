package org.dangcat.persistence.simulate.data;

/**
 * Booleanģ������
 *
 * @author dangcat
 */
public class BooleanSimulator extends ValueSimulator {
    public BooleanSimulator() {
        super(Boolean.class);
    }

    protected Object createValue(int index) {
        return index % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
    }
}

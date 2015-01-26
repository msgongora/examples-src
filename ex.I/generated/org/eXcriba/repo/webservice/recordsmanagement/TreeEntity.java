/**
 * TreeEntity.java
 *
    public TreeEntity()
    {
    }

    public TreeEntity(String name)
    {
        super(name);
    }

    public int size()
    {
        
        return cantReal;
    }
    
    public TreeEntity(java.lang.String name,
            org.eXcriba.repo.webservice.recordsmanagement.TreeEntity[] entities)
    {
        super(name);
        this.entities = entities;
    }

    /**
     * Gets the entities value for this TreeEntity.
     * 
     * @return entities
     */
    public org.eXcriba.repo.webservice.recordsmanagement.TreeEntity[] getEntities()
    {
        return entities;
    }

    /**
     * @param entity
     */
    public void addEntity(String entity)
    {
        if (this.entities == null)
        {
            this.entities = new TreeEntity[CAPACITY];
        }
        int length = entities.length;
        if (cantReal == length - 1)
        {
            TreeEntity[] tmp = new TreeEntity[2 * length];
            for (int i = 0; i < length; i++)
            {
                tmp[i] = entities[i];
            }
            this.entities = null;
            entities = new TreeEntity[2 * length];
            for (int i = 0; i < tmp.length; i++)
            {
                entities[i] = tmp[i];
            }
            tmp = null;
        } 
        this.entities[cantReal] = new TreeEntity(entity);
        cantReal++;
    }

    /**
     * Sets the entities value for this TreeEntity.
     * 
     * @param entities
     */
    public void setEntities(org.eXcriba.repo.webservice.recordsmanagement.TreeEntity[] entities)
    {
        this.entities = entities;
    }

    public org.eXcriba.repo.webservice.recordsmanagement.TreeEntity getEntities(int i)
    {
        return this.entities[i];
    }

    public void setEntities(int i, org.eXcriba.repo.webservice.recordsmanagement.TreeEntity _value)
    {
        this.entities[i] = _value;
    }

    private java.lang.Object __equalsCalc = null;

    public synchronized boolean equals(java.lang.Object obj)
    {
        if (!(obj instanceof TreeEntity))
            return false;
        TreeEntity other = (TreeEntity) obj;
        if (obj == null)
            return false;
        if (this == obj)
            return true;
        if (__equalsCalc != null)
        {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj)
                && ((this.entities == null && other.getEntities() == null) || (this.entities != null && java.util.Arrays
                        .equals(this.entities, other.getEntities())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;

    public synchronized int hashCode()
    {
        if (__hashCodeCalc)
        {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getEntities() != null)
        {
            for (int i = 0; i < java.lang.reflect.Array.getLength(getEntities()); i++)
            {
                java.lang.Object obj = java.lang.reflect.Array.get(getEntities(), i);
                if (obj != null && !obj.getClass().isArray())
                {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(
            TreeEntity.class, true);

    static
    {
        typeDesc.setXmlType(new javax.xml.namespace.QName(
                "http://www.eXcriba.org/ws/service/recordsmanagement/1.0", "TreeEntity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("entities");
        elemField.setXmlName(new javax.xml.namespace.QName(
                "http://www.eXcriba.org/ws/service/recordsmanagement/1.0", "entities"));
        elemField.setXmlType(new javax.xml.namespace.QName(
                "http://www.eXcriba.org/ws/service/recordsmanagement/1.0", "TreeEntity"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc()
    {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType,
            java.lang.Class _javaType, javax.xml.namespace.QName _xmlType)
    {
        return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType,
            java.lang.Class _javaType, javax.xml.namespace.QName _xmlType)
    {
        return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
    }

}

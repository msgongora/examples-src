/**
 * CCEntity.java
 *

    public CCEntity(
           java.lang.String name,
           java.lang.String authorityUuid,
           boolean isContributor) {
        super(
            name);
        this.authorityUuid = authorityUuid;
        this.isContributor = isContributor;
    }


    /**
     * Gets the authorityUuid value for this CCEntity.
     * 
     * @return authorityUuid
     */
    public java.lang.String getAuthorityUuid() {
        return authorityUuid;
    }


    /**
     * Sets the authorityUuid value for this CCEntity.
     * 
     * @param authorityUuid
     */
    public void setAuthorityUuid(java.lang.String authorityUuid) {
        this.authorityUuid = authorityUuid;
    }


    /**
     * Gets the isContributor value for this CCEntity.
     * 
     * @return isContributor
     */
    public boolean isIsContributor() {
        return isContributor;
    }


    /**
     * Sets the isContributor value for this CCEntity.
     * 
     * @param isContributor
     */
    public void setIsContributor(boolean isContributor) {
        this.isContributor = isContributor;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof CCEntity)) return false;
        CCEntity other = (CCEntity) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = super.equals(obj) && 
            ((this.authorityUuid==null && other.getAuthorityUuid()==null) || 
             (this.authorityUuid!=null &&
              this.authorityUuid.equals(other.getAuthorityUuid()))) &&
            this.isContributor == other.isIsContributor();
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = super.hashCode();
        if (getAuthorityUuid() != null) {
            _hashCode += getAuthorityUuid().hashCode();
        }
        _hashCode += (isIsContributor() ? Boolean.TRUE : Boolean.FALSE).hashCode();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(CCEntity.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0", "CCEntity"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("authorityUuid");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0", "authorityUuid"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isContributor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.eXcriba.org/ws/service/recordsmanagement/1.0", "isContributor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "boolean"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}

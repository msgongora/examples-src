/**
 * RecordsManagementServiceSoapPort.java
 *

    /**
     * Delete entities.
     */
    public void deleteEntities(java.lang.String[] entities) throws java.rmi.RemoteException, org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementFault;

    /**
     * Adds child entitie to a specified parent entity.
     */
    public void addChildEntities(java.lang.String parentEntity, java.lang.String[] entities) throws java.rmi.RemoteException, org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementFault;

    /**
     * Remove child entities from the specified parent entity.
     */
    public void removeChildEntities(java.lang.String parentEntity, java.lang.String[] entities) throws java.rmi.RemoteException, org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementFault;

    /**
     * Gets all the entities avaialble in the repository.
     */
    public org.eXcriba.repo.webservice.recordsmanagement.TreeEntity getAllEntities() throws java.rmi.RemoteException, org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementFault;

    /**
     * Creates or update the business classification scheme for the
     * Institution.
     */
    public void ccSynchronization(org.eXcriba.repo.webservice.recordsmanagement.TreeFolder cclassification) throws java.rmi.RemoteException, org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementFault;

    /**
     * Gets a list of possible classification system.
     */
    public org.eXcriba.repo.webservice.recordsmanagement.TreeFolder[] getCCTemplates() throws java.rmi.RemoteException, org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementFault;

    /**
     * Get the classification system.
     */
    public org.eXcriba.repo.webservice.recordsmanagement.TreeFolder getCClassification() throws java.rmi.RemoteException, org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementFault;
}

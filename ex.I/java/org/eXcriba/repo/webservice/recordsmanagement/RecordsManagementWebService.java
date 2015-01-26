/*
 * Copyright (C) 2008-2009 _replaced_.
 *
 */
package org.eXcriba.repo.webservice.recordsmanagement;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.alfresco.webservice.accesscontrol.ACE;
import org.alfresco.webservice.accesscontrol.ACL;
import org.alfresco.webservice.accesscontrol.AccessControlFault;
import org.alfresco.webservice.accesscontrol.AccessControlServiceSoapBindingStub;
import org.alfresco.webservice.accesscontrol.AccessStatus;
import org.alfresco.webservice.accesscontrol.NewAuthority;
import org.alfresco.webservice.action.Action;
import org.alfresco.webservice.action.Condition;
import org.alfresco.webservice.action.Rule;
import org.alfresco.webservice.authentication.AuthenticationFault;
import org.alfresco.webservice.content.Content;
import org.alfresco.webservice.content.ContentFault;
import org.alfresco.webservice.content.ContentServiceSoapBindingStub;
import org.alfresco.webservice.repository.QueryResult;
import org.alfresco.webservice.repository.RepositoryFault;
import org.alfresco.webservice.repository.RepositoryServiceSoapBindingStub;
import org.alfresco.webservice.repository.UpdateResult;
import org.alfresco.webservice.types.CML;
import org.alfresco.webservice.types.CMLCreate;
import org.alfresco.webservice.types.CMLDelete;
import org.alfresco.webservice.types.NamedValue;
import org.alfresco.webservice.types.Node;
import org.alfresco.webservice.types.ParentReference;
import org.alfresco.webservice.types.Predicate;
import org.alfresco.webservice.types.Query;
import org.alfresco.webservice.types.Reference;
import org.alfresco.webservice.types.ResultSet;
import org.alfresco.webservice.types.ResultSetRow;
import org.alfresco.webservice.types.ResultSetRowNode;
import org.alfresco.webservice.types.Store;
import org.alfresco.webservice.util.AuthenticationUtils;
import org.alfresco.webservice.util.Constants;
import org.alfresco.webservice.util.ContentUtils;
import org.alfresco.webservice.util.ISO9075;
import org.alfresco.webservice.util.Utils;
import org.alfresco.webservice.util.WebServiceFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eXcriba.config.xml.XmlParser;
import org.eXcriba.repo.webservice.CCEntity;

/**
 * Web service implementation of the RecordsManagementService. The WSDL for this
 * service can be accessed from
 * http://localhost:8080/alfresco/wsdl/records-management-service.wsdl
 * 
 * @author Marcel S. Gongora - <mrsanchez@uci.cu>
 */
public class RecordsManagementWebService
{

    /** Log */
    private static Log logger = LogFactory.getLog(RecordsManagementWebService.class);

    public static String AUTHORITY_ROOT = "/sys:system/sys:authorities";
    public static String EXCRIBA_ROOT = "/app:company_home/cm:eXcriba";
    public static String APP_ROOT = "/app:company_home";
    public static String[] EXCRIBA_PLACES = { "eXcriba", "conf", "incoming", "series", "templates", "cc",
            "workspaces" };
    public String CURRENT_WS_PATH;
    public String CURRENT_AT_PATH;

    public static String AUTHORITY_TYPE = "GROUP";
    public static String GROUP_PREFIX = "GROUP_";

    public static String[] PERMISSION = { "Consumer", "Editor", "Contributor", "Collaborator", "Coordinator",
            "All" };

    private Store spacesStore;
    private Store userStore;
    private RepositoryServiceSoapBindingStub repositoryService;
    private AccessControlServiceSoapBindingStub accessControlService;
    private ContentServiceSoapBindingStub contentService;

 
    /**
     * Set the permissions service
     * 
     * @param permissionService
     *            the permissions service
     * @throws AuthenticationFault
     */
    // public void setPermissionService(PermissionService permissionService)
    // {
    // this.permissionService = permissionService;
    // }
    public RecordsManagementWebService() throws AuthenticationFault
    {
        WebServiceFactory.setEndpointAddress("http://10.3.10.42:8080/alfresco/api");
        AuthenticationUtils.startSession("admin", "newadmin1");
        spacesStore = new Store(Constants.WORKSPACE_STORE, "SpacesStore");
        userStore = new Store("user", "alfrescoUserStore");
        repositoryService = WebServiceFactory.getRepositoryService();
        accessControlService = WebServiceFactory.getAccessControlService();
        contentService = WebServiceFactory.getContentService();
    }

    /**
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementServiceSoapPort#addChildEntities(java.lang.String,
     *      java.lang.String[])
     */
    public void addChildEntities(String parentEntity, String[] entities) throws RemoteException
    {
        // TODO Auto-generated method stub

    }

    /**
     * 
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementServiceSoapPort#ccSynchronization(org.eXcriba.repo.webservice.recordsmanagement.TreeFolder)
     */
    public void ccSynchronization(TreeFolder cclassification) throws RemoteException
    {
        Map<String, String[]> series = createSeries(cclassification);

        // creamos las reglas asociadas
        Node incomingFol = createNodeByPath(EXCRIBA_ROOT + "/cm:" + EXCRIBA_PLACES[2]);
        String serieUuid;
        NamedValue propDestinationFolder;
        NamedValue propAssocType;
        NamedValue propAssocName;
        Action newAction = new Action();
        Condition[] condition;
        // Create the composite action
        Action action = new Action();
        newAction.setActionName("move");
        Set<Entry<String, String[]>> set = series.entrySet();
        
        int cantRules = series.size();
        Rule[] newRule = new Rule[cantRules];
        
        Iterator<Entry<String, String[]>> iter = set.iterator();
        Entry<String, String[]> entry;
        while (iter.hasNext())
        {
            entry = iter.next();
            String sName = entry.getKey();
            serieUuid = createNodeByPath(EXCRIBA_ROOT + "/cm:" + EXCRIBA_PLACES[3] + "/cm:" + sName)
                    .getReference().getUuid();
            propDestinationFolder = Utils.createNamedValue("destination-folder", "workspace://SpacesStore/"
                    + serieUuid);
            propAssocType = Utils.createNamedValue("assoc-type", "{" + Constants.NAMESPACE_CONTENT_MODEL
                    + "}contains");
            propAssocName = Utils.createNamedValue("assoc-name", "{" + Constants.NAMESPACE_CONTENT_MODEL
                    + "}move");
            newAction.setParameters(new NamedValue[] { propDestinationFolder, propAssocName, propAssocType });

            // creamos una condicion para cada tipo de contenido
            int length = entry.getValue().length;
            condition = new Condition[length];
            for (int i = 0; i < length; i++)
            {
                condition[i] = new Condition();
                condition[i].setConditionName("is-subtype");
                condition[i].setParameters(new NamedValue[] { Utils.createNamedValue("type",
                        entry.getValue()[i]) });
            }
            action.setActionName("composite-action");
            action.setActions(new Action[] { newAction });
            action.setConditions(condition);
            // inicializamos la regla
            
            newRule[--cantRules] = new Rule();
            newRule[cantRules].setRuleTypes(new String[] { "inbound" });
            newRule[cantRules].setTitle("Contenidos de la serie " + sName);
            newRule[cantRules].setDescription("Esta regla es para los nuevos contenidos que se depositarán en la serie "
                    + sName);
            newRule[cantRules].setAction(action);
            newRule[cantRules].setOwningReference(incomingFol.getReference());
            
        }
        WebServiceFactory.getActionService().saveRules(incomingFol.getReference(), newRule );
    }

    /**
     * Crea un nodo dada la ruta.
     * 
     * @param path
     * @return
     */
    private Node createNodeByPath(String path)
    {
        try
        {
            return repositoryService.get(new Predicate(new Reference[] { new Reference(spacesStore, null,
                    path) }, spacesStore, null))[0];
        } catch (RepositoryFault e)
        {
            if (logger.isDebugEnabled())
            {
                logger.error("Unexpected error while processing the Repository: ", e);
            }
        } catch (RemoteException e)
        {
            if (logger.isDebugEnabled())
            {
                logger.error("Unexpected error while processing the RMI: ", e);
            }
        }
        return null;
    }

    /**
     * Crea los espacios de trabajo que representaran las series.
     * 
     * @param cc
     * @return
     * @throws AccessControlFault
     * @throws RemoteException
     */
    public Map<String, String[]> createSeries(TreeFolder cc) throws AccessControlFault, RemoteException
    {
        List<TreeFolder> folders = cc.getFolders();
        Map<String, String[]> serieContents = new HashMap<String, String[]>();
        for (TreeFolder currentSerie : folders)
        {
            if (currentSerie.getType() == null || !currentSerie.getType().equals("serie"))
                serieContents.putAll(createSeries(currentSerie));
            else
            {
                String root = EXCRIBA_ROOT + "/cm:" + EXCRIBA_PLACES[3];
                String sName = currentSerie.getId();
                serieContents.put(sName, currentSerie.getContentsName());

                ParentReference parentRef = new ParentReference(spacesStore, null, root,
                        Constants.ASSOC_CONTAINS, Constants.createQNameString(
                                Constants.NAMESPACE_CONTENT_MODEL, sName));
                NamedValue[] properties = new NamedValue[] { Utils.createNamedValue(Constants.PROP_NAME,
                        sName) };

                CMLCreate create = new CMLCreate("1", parentRef, null, null, null, Constants.TYPE_FOLDER,
                        properties);

                CML cml = new CML();
                cml.setCreate(new CMLCreate[] { create });
                UpdateResult[] results = null;
                try
                {
                    results = repositoryService.update(cml);
                } catch (RepositoryFault e)
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.error("Unexpected error while processing the Repository: ", e);
                    }
                    e.printStackTrace();
                } catch (RemoteException e)
                {
                    if (logger.isDebugEnabled())
                    {
                        logger.error("Unexpected error while processing the RMI: ", e);
                    }
                    e.printStackTrace();
                }
                List<CCEntity> ids = currentSerie.getEntities();
                for (int i = 0; i < ids.size(); i++)
                {
                    setPermission(root + "/cm:" + sName, ids.get(i).getId(), PERMISSION[0], true, false);
                }
                // dumpUpdateResults(results);
            }
        }
        return serieContents;
    }

    /**
     * Devuelve una lista de las posibles cuadros de clasificación.
     * 
     * @return
     * @throws RemoteException
     * @throws ContentFault
     */
    public TreeFolder[] getTemplates() throws ContentFault, RemoteException
    {

        ResultSetRowNode[] nodes = getChilds(EXCRIBA_ROOT + "/cm:" + EXCRIBA_PLACES[4] + "/cm:"
                + EXCRIBA_PLACES[5]);
        TreeFolder[] templates = new TreeFolder[nodes.length];
        for (int i = 0; i < nodes.length; i++)
        {
            Reference ref = new Reference(spacesStore, nodes[i].getId(), null);
            Content[] readResult2 = contentService.read(new Predicate(new Reference[] { ref }, spacesStore,
                    null), Constants.PROP_CONTENT);
            Content content2 = readResult2[0];
            templates[i] = new XmlParser().parsingCC(ContentUtils.getContentAsString(content2));
            // System.out.println("Template #" + i);
            // System.out.println(ContentUtils.getContentAsString(content2));
            // System.out.println("***************************");
        }
        return templates;
    }

    /**
     * Retorna los nodos hijos que existen en la <b>ruta</b> dada.
     * 
     * @param path
     * @return
     */
    private ResultSetRowNode[] getChilds(String path)
    {
        // creamos la referencia al contenido en path
        Reference parentRf = new Reference(spacesStore, null, path);
        QueryResult queryResult = null;
        try
        {
            // Obtenemos los contenidos hijos del directorio padre
            // TODO - Revisar el tipo de excepcion que devulve el metodo
            // queryChildren
            queryResult = repositoryService.queryChildren(parentRf);

        } catch (RepositoryFault e)
        {
            if (logger.isDebugEnabled())
            {
                logger.error("Unexpected error while processing the Repository: ", e);
            }
            e.printStackTrace();
        } catch (RemoteException e)
        {
            if (logger.isDebugEnabled())
            {
                logger.error("Unexpected error while processing the RMI: ", e);
            }
            e.printStackTrace();
        }
        ResultSet resultSet = queryResult.getResultSet();
        int length = 0;
        try
        {
            length = (int) resultSet.getTotalRowCount();
        } catch (ClassCastException e)
        {
            if (logger.isDebugEnabled())
            {
                logger.error("The total row of ResultSet is too big for convert to int type: ", e);
            }
            e.getStackTrace();
        }
        ResultSetRowNode[] resulstRNode = null;
        if (length > 0)
        {
            ResultSetRow[] resultR = resultSet.getRows();
            resulstRNode = new ResultSetRowNode[length];
            for (int i = 0; i < length; i++)
            {
                resulstRNode[i] = resultR[i].getNode();
            }

        }
        return resulstRNode;
    }

    /**
     * Crea las entidades del sistema, las cuales se traducen en grupos y
     * espacios de trabajo.
     * 
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagem for
     *      (int i = 0; i < array.length; i++) { row.getNode(); }
     *      Port#createEntities(java.lang.String,
     *      org.eXcriba.repo.webservice.recordsmanagement.TreeEntity)
     */
    public void createEntities(String patentEntity, TreeEntity ent) throws RemoteException
    {
        // Verifico si es la Entity root
        if (patentEntity == null)
        {
            patentEntity = ent.getName();
            System.out.println("Padre root " + patentEntity);

            NewAuthority newAuth = new NewAuthority(AUTHORITY_TYPE, patentEntity);
            String[] result1 = this.accessControlService.createAuthorities(null,
                    new NewAuthority[] { newAuth });
            dumpUpdateResults(result1);
            // crear el workspace para esta entidad
            CURRENT_WS_PATH = EXCRIBA_ROOT + "/cm:" + EXCRIBA_PLACES[6];
            createWorkSpace(patentEntity, CURRENT_WS_PATH);
            setPermission(CURRENT_WS_PATH + "/cm:" + patentEntity, getAuthorityUuid(patentEntity),
                    PERMISSION[6], true, false);

        }
        // Si la treeEntity no tiene mas hijos entonces se detiene el proceso.
        if (ent.getEntities() != null)
        {
            createChildEntities(patentEntity, ent.getEntities());
        }

    }

    /**
     * 
     * @param parentEntity
     * @param chidren
     * @throws AccessControlFault
     * @throws RemoteException
     */
    private void createChildEntities(String parentEntity, TreeEntity[] chidren) throws AccessControlFault,
            RemoteException
    {
        CURRENT_WS_PATH += "/cm:" + ISO9075.encode(parentEntity);
        int length = sizeChild(chidren);
        String[] childrenName = new String[length];
        NewAuthority[] childrenAuth = new NewAuthority[length];
        for (int i = 0; i < length; i++)
        {
            childrenName[i] = chidren[i].getName();
            System.out.println("\tHijo: " + childrenName[i] + " *** Padre: " + parentEntity);
            childrenAuth[i] = new NewAuthority(AUTHORITY_TYPE, childrenName[i]);

            createWorkSpace(childrenName[i], CURRENT_WS_PATH);
        }
        String[] result2 = this.accessControlService.createAuthorities(null, childrenAuth);
        for (int i = 0; i < length; i++)
        {
            setPermission(CURRENT_WS_PATH + "/cm:" + ISO9075.encode(childrenName[i]),
                    getAuthorityUuid(childrenName[i]), PERMISSION[6], true, true);
        }
        // dumpUpdateResults(result2);
        for (int i = 0; i < length; i++)
        {
            createEntities(chidren[i].getName(), chidren[i]);

        }
    }

    /**
     * Borra entidades.
     * 
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementServiceSoapPort#deleteEntities(java.lang.String[])
     */
    public void deleteEntities(String[] entities) throws RemoteException, RecordsManagementFault
    {
        // TODO Auto-generated method stub

    }

    /**
     * Retorna el <code>uuid</code> del grupo.
     * 
     * @param name
     * @return
     * @throws RepositoryFault
     * @throws RemoteException
     */
    public String getAuthorityUuid(String name) throws RepositoryFault, RemoteException
    {
        QueryResult queryResult = null;
        Reference[] authorityRf = new Reference[] { new Reference(userStore, null, AUTHORITY_ROOT + "/usr:"
                + GROUP_PREFIX + ISO9075.encode(name)) };
        queryResult = repositoryService.queryChildren(authorityRf[0]);

        Predicate pred = new Predicate(authorityRf, userStore, null);

        Content[] readResult = contentService.read(new Predicate(authorityRf, userStore, null),
                Constants.PROP_CONTENT);

        Node[] authorityNd = repositoryService.get(pred);

        NamedValue[] prop = authorityNd[0].getProperties();
        String authorityUuid = null;
        for (NamedValue namedValue : prop)
        {
            if (namedValue.getName().equals("{http://www.alfresco.org/model/system/1.0}node-uuid"))
            {
                authorityUuid = namedValue.getValue();
                break;
            }
        }
        return authorityUuid;
    }

    /**
     * Asigna el permiso especificado al espacio de trabajo pasado por
     * parámetro.
     * 
     * @param path
     * @param uuid
     * @param permission
     * @param status
     * @param inherit
     * @throws AccessControlFault
     * @throws RemoteException
     */
    public void setPermission(String path, String uuid, String permission, boolean status, boolean inherit)
            throws AccessControlFault, RemoteException
    {
        // buscamos el nombre del grupo
        Reference pathRef = new Reference(spacesStore, null, path);
        Reference[] entityRef = new Reference[] { new Reference(userStore, uuid, null) };
        Predicate pre = new Predicate(entityRef, userStore, null);
        Node[] entity = repositoryService.get(pre);
        NamedValue[] aux = entity[0].getProperties();
        String authorityName = null;
        //
        for (NamedValue namedValue : aux)
        {
            if (namedValue.getName().equals("{http://www.alfresco.org/model/user/1.0}authorityName"))
            {
                authorityName = namedValue.getValue();
                break;
            }
        }
        Predicate predicate = new Predicate(new Reference[] { pathRef }, spacesStore, null);
        // obtendo la lista de control de acceso del nodo
        ACL[] acl = accessControlService.getACLs(predicate, null);
        if (inherit)
        {
            if (!acl[0].isInheritPermissions())
                accessControlService.setInheritPermission(predicate, true);
        } else
        {
            if (acl[0].isInheritPermissions())
                accessControlService.setInheritPermission(predicate, false);
        }
        // Obtengo las entradas de control de acceso
        // An Access Control Entry (ACE) contains the information about the
        // permission and how it relates to an authority.
        ACE[] ace = acl[0].getAces();
        AccessStatus access;
        if (status)
        {
            access = AccessStatus.acepted;
        } else
        {
            access = AccessStatus.declined;
        }
        // retiro los permisos para GROUP_EVERYONE
        if (ace != null)
        {
            for (int i = 0; i < ace.length; i++)
            {
                if (ace[i].getAuthority().equals("GROUP_EVERYONE"))
                {
                    accessControlService.removeACEs(predicate, new ACE[] { ace[i] });
                }
            }
        }
        ACE newAces = new ACE(authorityName, permission, access);
        accessControlService.addACEs(predicate, new ACE[] { newAces });
    }

    /**
     * Retorna el tamaño del arreglo.
     * 
     * @param child
     * @return
     */
    private int sizeChild(TreeEntity[] child)
    {
        int count = 0;
        for (int i = 0; i < child.length; i++)
        {
            if (child[i] != null)
            {
                count++;
            } else
            {
                break;
            }
        }
        return count;
    }

    /**
     * Crea un espacio de trabajo en la ruta especificada.
     * 
     * @param name
     * @param path
     */
    public void createWorkSpace(String name, String path)
    {
        // TODO - arreglar el tema de los caracteres especiales con ISO9075;
        ParentReference parentReference = new ParentReference(spacesStore, null, path,
                Constants.ASSOC_CONTAINS, Constants
                        .createQNameString(Constants.NAMESPACE_CONTENT_MODEL, name));
        NamedValue[] properties = new NamedValue[] { Utils.createNamedValue(Constants.PROP_NAME, name) };

        CMLCreate create = new CMLCreate("1", parentReference, null, null, null, Constants.TYPE_FOLDER,
                properties);

        CML cml = new CML();
        cml.setCreate(new CMLCreate[] { create });
        UpdateResult[] results = null;
        try
        {
            results = repositoryService.update(cml);
        } catch (RepositoryFault e)
        {
            if (logger.isDebugEnabled())
            {
                logger.error("Unexpected error while processing the Repository: ", e);
            }
            e.printStackTrace();
        } catch (RemoteException e)
        {
            if (logger.isDebugEnabled())
            {
                logger.error("Unexpected error while processing the RMI: ", e);
            }
            e.printStackTrace();
        }
    }

    /**
     * 
     * @param path
     * @throws RepositoryFault
     * @throws RemoteException
     */
    public void getWorkSpacePathUuid(String path) throws RepositoryFault, RemoteException
    {
        String texto_query = "+PATH:\"" + path + "\"";
        System.out.println("Query para obtener id de nodo: " + texto_query);
        Query query = new Query(Constants.QUERY_LANG_LUCENE, texto_query);
        QueryResult queryResult = repositoryService.query(spacesStore, query, false);
        ResultSet resultSet = queryResult.getResultSet();
        ResultSetRow[] rows = resultSet.getRows();
        if (rows != null)
        {
            for (ResultSetRow row : rows)
            {
                String id = row.getNode().getId();
                // String val = row.getNode().get
                System.out.println("Encontrado store de usuarios con id: " + id);
                // Reference reference = new Reference(spacesStore, id, null);
            }
        }

    }

    public String getWorkSpacePath(String name)
    {
        // repositoryService.q
        // String path = "/app:company_home/cm:para_x0020_Jaime";
        //        
        // // Montamos la query para encontrar el id de nodo
        // String texto_query = "+PATH:\"" + path + "\"";
        // System.out.println("Query para obtener id de nodo: " + texto_query);
        // Query query = new Query(Constants.QUERY_LANG_LUCENE, texto_query);
        //
        // try {
        // // Ejecutamos la consulta para encontrar la bustia del usuario
        // QueryResult queryResult = repositoryService.query(STORE, query,
        // false);
        //
        // // Mostramos los resultados...
        // ResultSet resultSet = queryResult.getResultSet();
        // ResultSetRow[] rows = resultSet.getRows();
        //
        // if (rows != null) {
        // // Obtenemos los registros de la query...en principio sólo 1...
        // for (ResultSetRow row : rows) {
        // String id = row.getNode().getId();
        // System.out.println("Encontrado store de usuarios con id: " + id);
        // Reference reference = new Reference(STORE, id, null);
        //                    
        // NewUserDetails[] newUsers = new NewUserDetails[] { new
        // NewUserDetails(
        // usuario, password, createPersonProperties(
        // "workspace://SpacesStore/" + reference.getUuid(),
        // nombre, apellido1, apellido2, email, nombre)) };
        //                      
        // WebServiceFactory.getAdministrationService().createUsers(newUsers);
        // System.out.println("Usuario creado");
        // }
        // }
        // } catch (java.rmi.RemoteException exp) {
        // System.out.println("ERROR al realizar la consulta");
        // exp.printStackTrace();
        // }
        return "";
    }

    /**
     * Limpia todo un espacio de trabajo dada su <b>ruta</b>.
     * 
     * @param path
     * @throws RemoteException
     * @throws RepositoryFault
     */
    public void cleanWorkSpace(String path) throws RepositoryFault, RemoteException
    {
        // Recorremos cada nodo hijo encontrado
        ResultSetRowNode[] nodes = getChilds(path);
        int length = nodes.length;
        CMLDelete[] deleteCMLArray = new CMLDelete[length];
        for (int i = 0; i < length; i++)
        {
            deleteCMLArray[i] = new CMLDelete(new Predicate(new Reference[] { new Reference(spacesStore,
                    nodes[i].getId(), null) }, null, null));
        }
        CML cml = new CML();
        cml.setDelete(deleteCMLArray);
        UpdateResult[] results = repositoryService.update(cml);
    }

    public void createSystem()
    {
        createWorkSpace(EXCRIBA_PLACES[0], APP_ROOT);
        for (int i = 1; i < EXCRIBA_PLACES.length; i++)
        {
            createWorkSpace(EXCRIBA_PLACES[i], EXCRIBA_ROOT);
        }

    }

    private void dumpUpdateResults(String[] result)
    {
        for (String res : result)
        {
            System.out.println("grupo: " + res);

        }
    }

    /**
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementServiceSoapPort#getAllEntities()
     */
    public TreeEntity getAllEntities() throws RemoteException, RecordsManagementFault
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementServiceSoapPort#getCCTemplates()
     */
    public TreeFolder[] getCCTemplates() throws RemoteException, RecordsManagementFault
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementServiceSoapPort#getCClassification()
     */
    public TreeFolder getCClassification() throws RemoteException, RecordsManagementFault
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see org.eXcriba.repo.webservice.recordsmanagement.RecordsManagementServiceSoapPort#removeChildEntities(java.lang.String,
     *      java.lang.String[])
     */
    public void removeChildEntities(String parentEntity, String[] entities) throws RemoteException,
            RecordsManagementFault
    {
        // TODO Auto-generated method stub

    }

    /**
     * @see java.lang.Object#finalize()
     */
    @Override
    protected void finalize() throws Throwable
    {
        // TODO Auto-generated method stub
        super.finalize();
        AuthenticationUtils.endSession();
    }

}

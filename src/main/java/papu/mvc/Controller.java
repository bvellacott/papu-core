package papu.mvc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public abstract class Controller
<
M,
I
>
implements
IController<M, I>
{
	public EntityManager createEntityManager() throws Exception
	{
		return getEntityManagerFactory().createEntityManager();
	}
	
	public M createModel(M aMsg) throws Exception
	{
		EntityManager em = createEntityManager();

		em.persist(aMsg);
		
		em.close();

		return aMsg;
	}

	protected List<M> findAllModels() throws Exception
	{
		EntityManager em = createEntityManager();
		
        CriteriaQuery<M> cq = em.getCriteriaBuilder().createQuery(this.getModelClass());
        cq.select(cq.from(this.getModelClass()));
        List<M> list = em.createQuery(cq).getResultList();
        
        em.close();
        
        return list;
	}
	
	protected M findModel(I aId) throws Exception
	{
		EntityManager em = createEntityManager();
		
        M model = em.find(this.getModelClass(), aId);
        
        em.close();

        return model;
	}
	
	public M updateModel(M aMsg) throws Exception
	{
		EntityManager em = createEntityManager();
		
		M msg = em.merge(aMsg);
        
        em.close();
		
		return msg;
	}

	public M deleteModel(I id) throws Exception
	{
		EntityManager em = createEntityManager();
		
        M model = em.find(this.getModelClass(), id);
        em.remove(model);
        
        em.close();

        return model;
	}

	public static <T> T parse(String aParam, Class<T> aSimpleType) throws ParseException 
	{
		//TODO Parameterise date parsing
		
		Class<?> type = aSimpleType;

		if(type == String.class) return (T)aParam;
	    if(type == boolean.class || type == Boolean.class) return (T) new Boolean(aParam);
	    if(type == byte.class || type == Byte.class) return (T) new Byte(aParam);
	    if(type == char.class || type == Character.class) return (T)(Character)aParam.charAt(0);
	    if(type == double.class || type == Double.class) return (T) new Double(aParam);
	    if(type == float.class || type == Float.class) return (T) new Float(aParam);
	    if(type == int.class || type == Integer.class) return (T) new Integer(aParam);
	    if(type == long.class || type == Long.class) return (T) new Long(aParam);
	    if(type == short.class || type == Short.class) return (T) new Short(aParam);
	    if(type == java.util.Date.class) return (T) new java.util.Date(aParam);
	    if(type == java.sql.Date.class) return (T) java.sql.Date.valueOf(aParam);
	    if(type == BigDecimal.class) {
	    	DecimalFormat df = new DecimalFormat();
	    	df.setParseBigDecimal(true);
	    	return (T) new BigDecimal(aParam);
	    }
	    if(type == BigInteger.class) {
	    	DecimalFormat df = new DecimalFormat();
	    	df.setParseIntegerOnly(true);
	    	return (T) new BigInteger(aParam);
	    }
	    
		throw new IllegalArgumentException("The type argument passed is not a valid simple type accepted by the EJB specification!");
	}
}

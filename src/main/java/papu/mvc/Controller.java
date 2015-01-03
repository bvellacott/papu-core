package papu.mvc;

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
	
	public M create(M aMsg) throws Exception
	{
		EntityManager em = createEntityManager();

		em.persist(aMsg);
		
		em.close();

		return aMsg;
	}

	protected List<M> findAll(Class<M> aModelClass) throws Exception
	{
		EntityManager em = createEntityManager();
		
        CriteriaQuery<M> cq = em.getCriteriaBuilder().createQuery(aModelClass);
        cq.select(cq.from(aModelClass));
        List<M> list = em.createQuery(cq).getResultList();
        
        em.close();
        
        return list;
	}
	
	protected M find(I aId) throws Exception
	{
		EntityManager em = createEntityManager();
		
        M model = em.find(this.getModelClass(), aId);

        return model;
	}
	
	public M update(I aId, M aMsg) throws Exception
	{
		EntityManager em = createEntityManager();
		
		this.setModelId(aMsg, aId);
		
		M msg = em.merge(aMsg);
        
        em.close();
		
		return msg;
	}

	public M delete(I id, Class<M> aModelClass) throws Exception
	{
		EntityManager em = createEntityManager();
		
        M model = em.find(this.getModelClass(), id);
        em.remove(model);
        
        em.close();

        return model;
	}

}

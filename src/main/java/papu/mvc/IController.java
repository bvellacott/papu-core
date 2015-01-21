package papu.mvc;

import javax.persistence.EntityManagerFactory;


public interface IController<M,I>
{

	public Class<M> getModelClass();
	
	public EntityManagerFactory getEntityManagerFactory();
	
}

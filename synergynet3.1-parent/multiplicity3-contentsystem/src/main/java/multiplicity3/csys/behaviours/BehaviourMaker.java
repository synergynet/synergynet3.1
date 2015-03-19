package multiplicity3.csys.behaviours;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.logging.Logger;

import multiplicity3.csys.items.item.IItem;
import multiplicity3.csys.stage.IStage;

public class BehaviourMaker {

    private static final Logger log = Logger.getLogger(BehaviourMaker.class
            .getName());
    
	private IStage stage;
    
    public BehaviourMaker(IStage stage) {
    	this.stage = stage;
    }
   

    @SuppressWarnings("unchecked")
	public <Behaviour extends IBehaviour> Behaviour addBehaviour(IItem item,
            Class<Behaviour> behaviourClass) {
        try {
            IBehaviour behaviour = behaviourClass.newInstance();
            behaviour.setStage(stage);
            behaviour.setEventSource(item);
            behaviour.setItemActingOn(item);
            item.behaviourAdded(behaviour);
            return (Behaviour) behaviour;
        } catch (InstantiationException e) {
            log.warning(e.toString());
        } catch (IllegalAccessException e) {
            log.warning(e.toString());
        }
        return null;
    }
    
    public ArrayList<IBehaviour> getBehavior(IItem item, Class<? extends IBehaviour> behaviourClass) {
    	ArrayList<IBehaviour> behaviours = new ArrayList<IBehaviour>();
        if (!item.getBehaviours().isEmpty()) {
        	ListIterator<IBehaviour> li = item.getBehaviours().listIterator();
            
            while(li.hasNext()) {
            	IBehaviour b = li.next();
            	
            	if (b.getClass().equals(behaviourClass)) {
            		behaviours.add(b);

                }
            }   
        }
        return behaviours;
    }

    public void removeBehavior(IItem item, Class<? extends IBehaviour> behaviourClass) {
        if (!item.getBehaviours().isEmpty()) {

        	ListIterator<IBehaviour> li = item.getBehaviours().listIterator();
            
            while(li.hasNext()) {
            	IBehaviour b = li.next();
            	
            	if (b.getClass().equals(behaviourClass)) {
            	    b.setEventSource(null);
            	    b.setItemActingOn(null);
                    li.remove();
                }
            }
            
            
        }
    }
}

package de.metalcon.sdd.queue;

import de.metalcon.sdd.Entity;
import de.metalcon.sdd.Sdd;
import de.metalcon.sdd.exception.InvalidAttrException;
import de.metalcon.sdd.exception.InvalidTypeException;

public class UpdateJsonQueueAction extends QueueAction {

    private Entity entity;

    public UpdateJsonQueueAction(
            Sdd sdd,
            Entity entity) {
        super(sdd, entity.getId());
        this.entity = entity;
    }

    @Override
    public void runQueueAction() throws InvalidTypeException,
            InvalidAttrException {
        sdd.actionUpdateJson(entity);
    }

    @Override
    public QueueActionType getType() {
        return QueueActionType.updateJsonQueueAction;
    }

    @Override
    public int hashCode() {
        int hash = 91241;
        int mult = 251;

        hash = hash * mult + ((Long) id).hashCode();

        return hash;
    }

}

package model.editor.factory;

import model.world.components.WorldComponent;
import model.world.components.entity.character.npc.NPC;
import model.world.components.entity.character.npc.*;
import model.world.components.entity.objects.Objects;
import model.world.components.entity.objects.obstacles.Obstacles;

public class EntityFactory {
    public WorldComponent createEntity(WorldComponent worldComponentSelected, int y, int x) {
        if(worldComponentSelected instanceof NPC) return createNPC((NPC) worldComponentSelected, y,  x);
        else if(worldComponentSelected instanceof Objects) return createObjects((Objects) worldComponentSelected, y,  x);
        return null;
    }

    private Objects createObjects(Objects objSelected,int y,int x){
        if(objSelected instanceof Obstacles) return new Obstacles((Obstacles) objSelected,y,x);
        return null;
    }
    private NPC createNPC(NPC npcSelected, int y, int x){
        if (npcSelected instanceof Walker) return new Walker(y,x);
        if (npcSelected instanceof Bomber) return new Bomber(y,x);
        return null;
    }
}

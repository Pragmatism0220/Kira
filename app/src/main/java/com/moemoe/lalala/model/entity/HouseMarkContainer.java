package com.moemoe.lalala.model.entity;

import java.util.ArrayList;

/**
 * Created by Hygge on 2018/5/24.
 */

public class HouseMarkContainer {
    private ArrayList<MapEntity> container;

    public HouseMarkContainer(){
        container = new ArrayList<>();
    }

    public void addMark(MapEntity entity){container.add(entity);}

    public MapEntity getMark(int index){return container.get(index);}

    public MapEntity getMarkById(String id){
        for (MapEntity entity : container){
            if (entity.getId().equals(id)){
                return entity;
            }
        }
        return null;
    }

    public void removeMarkById(String id){
        MapEntity tmp =  null;
        for (MapEntity entity : container){
            if (entity.getId().equals(id)){
                tmp = entity;
                break;
            }
        }
        if(tmp != null){
            container.remove(tmp);
        }
    }

    public int size(){return container.size();}

    public ArrayList<MapEntity> getContainer(){
        return container;
    }
}

package ru.appline.controller;

import org.springframework.web.bind.annotation.*;
import ru.appline.logic.Pet;
import ru.appline.logic.PetModel;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class Controller {
    private static final PetModel petModel = PetModel.getInstance();
    private static final AtomicInteger newId = new AtomicInteger(1);
    /*
        {
            "name": "Pirojok",
            "type": "Popugai",
            "age": 2
        }
     */
    @PostMapping(value = "/createPet", consumes = "application/json", produces = "application/json")
    public Map<String, String> createPet(@RequestBody Pet pet){
        int id = newId.getAndIncrement();
        petModel.add(pet,id);
        Map<String, String> response = new HashMap<String, String>();
        if(id==1){
            response.put("message", "Поздравляем вы создали 1-го питомца");
        }else{
            response.put("message", "Поздравляем вы создали " +id+"-го питомца");
        }

        return response;

    }
    @GetMapping(value = "/getAll", produces = "application/json")
    public Map<Integer,Pet> getAll(){
        return petModel.getAll();
    }


    /*
        {
            "id": 3
        }
    */
    @GetMapping(value = "/getPet", consumes = "application/json", produces = "application/json")
    public Map<String, Object> getPet(@RequestBody Map<String,Integer> regBody){
        int id = regBody.get("id");
        Pet pet = petModel.getFromList(id);
        Map<String, Object> response = new HashMap<String, Object>();
        if (pet == null) {
            response.put("error", "Питомец с id=" + id + " не найден");
            return response;
        }
        response.put("name", pet.getName());
        response.put("type", pet.getType());
        response.put("age", pet.getAge());
        return response;
    }

    /*
        {
            "id": 3
        }
    */
    @DeleteMapping(value = "/deletePet", consumes = "application/json", produces = "application/json")
    public Map<String, String> DeletePet(@RequestBody Map<String,Integer> regBody){
        int id = regBody.get("id");
        newId.getAndDecrement();
        Map<String, String> response = new HashMap<String, String>();
        if(!petModel.getAll().containsKey(id)){
            response.put("message", "Питомца с id=" +id+" нет");
        }else {
        petModel.deletePet(id);
        response.put("message", "Питомец с id=" +id+" удален( ");
        }
        return response;
    }
    /*
        {
            "id": 1,
            "name": "Sergei",
            "type": "Bashkevich",
            "age": 21
        }
     */
    @PutMapping(value = "/updatePet", consumes = "application/json", produces = "application/json")
    public Map<String, String> UpdatePet(@RequestBody Map<String, Object> petData){
        int id = (Integer) petData.get("id");
        String name = (String) petData.get("name");
        String type = (String) petData.get("type");
        int age = (Integer) petData.get("age");
        newId.getAndDecrement();
        Map<String, String> response = new HashMap<String, String>();
        if(!petModel.getAll().containsKey(id)){
            response.put("message", "Питомца с id=" +id+" нет");
        }else {
            petModel.update(id, name, type, age);
            response.put("message", "Данные питомца с id=" +id+" обновлены");
        }
        return response;
    }

}

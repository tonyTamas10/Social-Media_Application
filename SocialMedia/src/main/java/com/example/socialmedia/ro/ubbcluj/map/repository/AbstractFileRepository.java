package com.example.socialmedia.ro.ubbcluj.map.repository;

import com.example.socialmedia.ro.ubbcluj.map.domain.Entity;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public abstract class AbstractFileRepository<ID, E extends Entity<ID>> extends InMemoryRepository<ID,E> {
    String fileName;

    protected AbstractFileRepository(String fileName) throws RepositoryException {
        super();
        this.fileName = fileName;
        loadData();

    }

    private void loadData() throws RepositoryException { //decorator pattern
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;
            while ((newLine = reader.readLine()) != null) {
                System.out.println(newLine);
                List<String> data = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(data);
                super.save(entity);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RepositoryException e) {
            throw new RepositoryException(e.toString());
        }
//        Path path= Paths.get(fileName);
//        try{
//            List<String> lines= Files.readAllLines(path);
//            lines.forEach(line -> {
//                E entity=extractEntity(Arrays.asList(line.split(";")));
//                super.save(entity);
//            });
//        }
//        catch (IOException e){
//            e.printStackTrace();
//        }

    }

    /**
     * extract entity  - template method design pattern
     * creates an entity of type E having a specified list of @code attributes
     *
     * @return an entity of type E
     */
    public abstract E extractEntity(List<String> attributes);  //Template Method


    protected abstract String createEntityAsString(E entity); //Template Method

    @Override
    public Optional<E> save(E entity) throws RepositoryException {
        Optional<E> result = super.save(entity);
        if (result.isEmpty())
            writeToFile(entity);
        return result;

    }

    protected void writeToFile(E entity) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {

            writer.write(createEntityAsString(entity));
            writer.newLine();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

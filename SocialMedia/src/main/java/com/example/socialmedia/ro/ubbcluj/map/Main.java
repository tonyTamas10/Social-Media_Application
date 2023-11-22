package com.example.socialmedia.ro.ubbcluj.map;

import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseConfig;
import com.example.socialmedia.ro.ubbcluj.map.config.DatabaseManager;
import com.example.socialmedia.ro.ubbcluj.map.domain.Friendship;
import com.example.socialmedia.ro.ubbcluj.map.domain.Tuple;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.domain.validators.UserValidator;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;
import com.example.socialmedia.ro.ubbcluj.map.repository.database.FriendshipDBRepository;
import com.example.socialmedia.ro.ubbcluj.map.repository.database.UserDBRepository;
import com.example.socialmedia.ro.ubbcluj.map.service.ServiceComponent;

import java.util.Optional;
import java.util.Scanner;
import java.util.UUID;

public class Main {
    static String RESET = "\u001B[0m";
    static String RED = "\u001B[31m";
    static String GREEN = "\u001B[32m";
    @SuppressWarnings("unused")
    static String YELLOW = "\u001B[33m";
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        // InMemoryRepository<UUID, User> userRepository = new InMemoryRepository<>();
        // InMemoryRepository<Tuple<UUID, UUID>, Friendship> friendshipRepository = new InMemoryRepository<>();

        DatabaseManager databaseManager = new DatabaseManager(DatabaseConfig.DB_URL, DatabaseConfig.DB_USER, DatabaseConfig.DB_PASS);

        // estabilishing a connection with the database for the user repo
        Repository<UUID, User> userDBRepository = new UserDBRepository(databaseManager);
        // estabilishing a connection with the database for the friendship repo
        Repository<Tuple<UUID, UUID>, Friendship> friendshipDBRepository = new FriendshipDBRepository(databaseManager, userDBRepository);

        UserValidator validator = new UserValidator();
        ServiceComponent service = new ServiceComponent(validator, userDBRepository, friendshipDBRepository);

        while(true) {

            System.out.println("\n\"exit\". Exit the program\n0. Add predefined values\n1. Add a new user\n2. Find a specific user by id\n3. Find every user registered\n4. Delete an user\n5. Add a new friend\n6. Remove a friend\n7. Show the number of communities\n8. Show the most sociable community\n");

            System.out.print("Your option is >> ");

            String option = scanner.next();

            switch (option) {
                case "exit" -> {
                    System.out.println("Thank you and have a good day");
                    return ;
                }
                case "0" -> addPredefinedValues(service);
                case "1" -> addEntity(service);
                case "2" -> findUser(service);
                case "3" -> findAllUsers(service);
                case "4" -> deleteUser(service);
                case "5" -> createFriendship(service);
                case "6" -> removeFriendship(service);
                case "7" -> numberOfCommunities(service);
                case "8" -> mostSociableCommunity(service);
                default -> System.out.println(RED + "\nWrong input option\n" + RESET);
            }
        }
    }

    public static void addPredefinedValues(ServiceComponent service) {
        try{
            System.out.print("Enter the case number >> ");
            int caseNumber = scanner.nextInt();
            service.addPredefinedValues(caseNumber);
            System.out.println(GREEN + "\nThe users have been added" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void findUser(ServiceComponent service) {
        try{
            System.out.print("ID of the user >> ");
            UUID id = UUID.fromString(scanner.next());
            System.out.println(GREEN + "\nThe user is: " + service.findOne(id).toString() + "\n" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void addEntity(ServiceComponent service) {
        try {
            System.out.print("First name >> ");
            String firstName = scanner.next();
            System.out.print("Last name >> ");
            String lastName = scanner.next();
            System.out.print("Email >> ");
            String email = scanner.next();
            User user = new User(firstName, lastName, email);
            service.save(user);
            System.out.println(GREEN + "\nThe user with the ID << " + user.getId().toString() + " >> has been added\n" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void findAllUsers(ServiceComponent service) {
        try {
            Iterable<User> users = service.findAll();
            System.out.println("These are all the users: \n");
//            for(User user : users) {
//                System.out.println(GREEN + user + RESET);
//            }
            users.forEach(user -> System.out.println(GREEN + user + RESET));
        } catch (Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void deleteUser(ServiceComponent service) {
        try {
            System.out.print("ID of the user >> ");
            UUID id = UUID.fromString(scanner.next());
            Optional<User> deletedUser = service.delete(id);
            System.out.println(GREEN + "\nThe user << " + deletedUser + " >> has been deleted\n" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void createFriendship(ServiceComponent service) {
        try {
            System.out.print("Enter your ID >> ");
            UUID idOfUser1 = UUID.fromString(scanner.next());

            System.out.print("Enter the ID of the user you want to be friends with  >> ");
            UUID idOfUser2 = UUID.fromString(scanner.next());

            service.createFriendship(service.findOne(idOfUser1).get(), service.findOne(idOfUser2).get(), service);

            System.out.println(GREEN + "\nYou are now friends\n" + RESET);
        } catch (Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void removeFriendship(ServiceComponent service) {
        try{
            System.out.print("Enter your ID >> ");
            UUID idOfUser1 = UUID.fromString(scanner.next());

            System.out.print("Enter the ID of the user you want to remove from your friends list >> ");
            UUID idOfUser2 = UUID.fromString(scanner.next());

            service.deleteFriendship(service.findOne(idOfUser1).get(), service.findOne(idOfUser2).get());

            System.out.println(GREEN + "\nYou are no longer friends\n" + RESET);
        } catch(Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void numberOfCommunities(ServiceComponent service) {
        try{
            System.out.println(GREEN + "\nNumber of communities >> " + service.numberOfCommunities() + RESET);
        } catch(Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }

    public static void mostSociableCommunity(ServiceComponent service) {
        try {
            System.out.println(GREEN + "\nThe most sociable community is >> \n" + service.mostSociableCommunity() + RESET);
        } catch (Exception e) {
            System.out.println(RED + "\n" + e.getMessage() + "\n" + RESET);
        }
    }
}

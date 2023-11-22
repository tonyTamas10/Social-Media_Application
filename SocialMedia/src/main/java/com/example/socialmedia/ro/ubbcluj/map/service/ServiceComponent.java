package com.example.socialmedia.ro.ubbcluj.map.service;

import com.example.socialmedia.ro.ubbcluj.map.domain.Entity;
import com.example.socialmedia.ro.ubbcluj.map.domain.Friendship;
import com.example.socialmedia.ro.ubbcluj.map.domain.Tuple;
import com.example.socialmedia.ro.ubbcluj.map.domain.User;
import com.example.socialmedia.ro.ubbcluj.map.domain.validators.UserValidator;
import com.example.socialmedia.ro.ubbcluj.map.repository.Repository;
import com.example.socialmedia.ro.ubbcluj.map.repository.RepositoryException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class ServiceComponent implements Service<UUID, User> {

    private final UserValidator validator;
    private final Repository<UUID, User> userRepository;
    private final Repository<Tuple<UUID, UUID>, Friendship> friendshipRepository;

    public ServiceComponent(UserValidator validator, Repository<UUID, User> userRepository, Repository<Tuple<UUID, UUID>, Friendship> friendshipRepository) {
        this.validator = validator;
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
    }

    public Optional<User> findOne(UUID id) throws ServiceException, RepositoryException {
        if(id == null)
            throw new ServiceException("Id must not be null");

        return userRepository.findOne(id);
    }

    public Iterable<User> findAll() throws ServiceException, RepositoryException {
        Iterable<User> entities = userRepository.findAll();
        AtomicLong size = new AtomicLong();
//        while(iterator.hasNext()) { // finding the number of entities stored
//            size++;
//            iterator.next();
//        }
        entities.forEach(user -> size.getAndIncrement());
        if (size.get() == 0) {
            throw new ServiceException("There are no entities"); // created a custom exception for service layer
        }

        return entities;
    }

    public User getUserByEmail(User user) throws RepositoryException {
        Iterable<User> users = userRepository.findAll();
        for(User other : users) {
            if(user.getEmail().equals(other.getEmail()))
                return user;
        }
        return null;
    }

    public void save(User entity) throws ServiceException, RepositoryException {
        validator.validate(entity);
        if(getUserByEmail(entity) != null) {
            throw new ServiceException("An user with the email << " + entity.getEmail() + " >> already exists");
        }

        if(userRepository.save(entity).isPresent()) {
            throw new ServiceException("The entity already exists");
        }
    }

    public Optional<User> delete(UUID id) throws ServiceException, RepositoryException {
        if(id == null)
            throw new ServiceException("Id must not be null");

        return userRepository.delete(id);
    }

    /**
     *
     * @param user1 - first user, must not be null
     * @param user2 - second user, must not be null
     * @param service - service component
     * @throws ServiceException - if the users don't exist or are null
     * @throws RepositoryException - if the users are already friends
     */
    public void createFriendship(User user1, User user2, ServiceComponent service) throws ServiceException, RepositoryException {

        validator.validate(user1);
        validator.validate(user2);

        if(user1.equals(user2)) {
            throw new ServiceException("There must be 2 different users");
        }
        var friendshipEntity = new Entity<Tuple<UUID, UUID>>(); // creating an entity with a tupple
        friendshipEntity.setId(new Tuple<>(user1.getId(), user2.getId())); // setting the id's of the users in the tuple
        Optional<Friendship> friendship = friendshipRepository.save(new Friendship(friendshipEntity, userRepository));

        if(friendship.isPresent()) {
            throw new ServiceException("These users are already friends");
        }

        user1.addFriend(user2);
        user2.addFriend(user1);
    }

    /**
     *
     * @param user1 - first user, must not be null
     * @param user2 - second user, must not be null
     * @throws ServiceException - if the users are the same
     */
    public void deleteFriendship(User user1, User user2) throws ServiceException, RepositoryException {
        validator.validate(user1);
        validator.validate(user2);

        if(user1.equals(user2)) {
            throw new ServiceException("There must be 2 different users");
        }

        AtomicReference<Optional<Friendship>> deletedFriendship = new AtomicReference<>(Optional.empty());

//        for(Friendship friendship : friendshipRepository.findAll()) {
//            if(friendship.getUser1().getId().equals(user1.getId()) && friendship.getUser2().getId().equals(user2.getId())) { // testing to see if the users have the same id as the entities from the friendship
//                deletedFriendship = friendshipRepository.delete(friendship.getId());
//                break;
//            }
//        }

        friendshipRepository.findAll().forEach(friendship -> {
            if (friendship.getUser1().getId().equals(user1.getId()) && friendship.getUser2().getId().equals(user2.getId())) {
                try {
                    deletedFriendship.set(friendshipRepository.delete(friendship.getId()));
                } catch (RepositoryException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        if(deletedFriendship.get().isEmpty()) {
            throw new ServiceException("You are not friends with this user");
        }

        user1.removeFriend(user2);
        user2.removeFriend(user1);

    }

    /**
     *
     * @return - all friendships
     * @throws ServiceException - if there are no frienships
     */
    public Iterable<Friendship> findAllFriendships() throws ServiceException, RepositoryException {
        Iterable<Friendship> friendships = friendshipRepository.findAll();

        if(!friendships.iterator().hasNext()) {
            throw new ServiceException("There are no friendships");
        }

        return friendships;
    }

    public void addPredefinedValues(int caseNumber) throws ServiceException, RepositoryException {
        switch (caseNumber) {
            case 1 -> {

                User u1 = new User("Marian", "Stina", "marian.stina@gmail.com", "parola");
                User u2 = new User("Gabi", "Pildesti", "gabi.pildesti@gmail.com", "parola");
                User u3 = new User("Tony", "Tamas", "tony.tamas@gmail.com", "parola");
                User u4 = new User("Stefan", "Tamas", "stefan.tamas@gmail.com", "parola");
                User u5 = new User("Ana", "Maria", "ana.maria@gmail.com", "parola");
                User u6 = new User("Nicu", "Robu", "nicu.robu@gmail.com", "parola");
                User u7 = new User("Nicu", "Andrei", "nicu.andrei@gmail.com", "parola");

                this.save(u1);
                this.save(u2);
                this.save(u3);
                this.save(u4);
                this.save(u5);
                this.save(u6);
                this.save(u7);

                this.createFriendship(u1, u2, this);
                this.createFriendship(u1, u7, this);

                this.createFriendship(u6, u5, this);
                this.createFriendship(u5, u4, this);
                this.createFriendship(u4, u3, this);
            }
            case 2 -> {

                User u1 = new User("Denisa", "Balan", "denisa.balan@gmail.com", "parola");
                User u2 = new User("Gabi", "Pildesti", "gabi.pildesti@gmail.com", "parola");
                User u3 = new User("Tony", "Tamas", "tony.tamas@gmail.com", "parola");
                User u4 = new User("Stefan", "Tamas", "stefan.tamas@gmail.com", "parola");
                User u5 = new User("Ana", "Maria", "ana.maria@gmail.com", "parola");
                User u6 = new User("Nicu", "Robu", "nicu.robu@gmail.com", "parola");
                User u7 = new User("Nicu", "Andrei", "nicu.andrei@gmail.com", "parola");

                this.save(u1);
                this.save(u2);
                this.save(u3);
                this.save(u4);
                this.save(u5);
                this.save(u6);
                this.save(u7);

                this.createFriendship(u1, u2, this);

                this.createFriendship(u6, u5, this);
                this.createFriendship(u5, u4, this);
                this.createFriendship(u4, u3, this);
                this.createFriendship(u7, u6, this);
            }
            default -> System.out.println("Enter a number from 1 to 3");
        }
    }

    /**
     *  find the number of communities formed
     * @return count - the number of communities
     */
    public int numberOfCommunities() throws RepositoryException {
        Iterable<User> users = userRepository.findAll();
        Set<User> userSet = new HashSet<>();
        AtomicInteger count = new AtomicInteger();

//        for(User user : users) {
//            if(!userSet.contains(user)) {
//                count++;
//                DFS(user, userSet);
//            }
//        }
        users.forEach(user -> {
            if(!userSet.contains(user)) {
                count.getAndIncrement();
                DFS(user, userSet);
            }
        });
        return count.get();
    }

    /**
     * depth-first search method
     * @param user -
     * @param userSet - adding to the set all the users that have a friend in common
     * @return - the list with all the related users
     */
    private List<User> DFS(User user, Set<User> userSet) {
        List<User> usersList= new ArrayList<>();
        usersList.add(user);
        userSet.add(user);

//        for(User friend : user.getFriends().values()) {
//            if(!userSet.contains(friend)) {
//                List<User> friendList = DFS(friend, userSet);
//                usersList.addAll(friendList);
//            }
//        }

        user.getFriends().forEach(friend -> {
            if(!userSet.contains(friend)) {
                List<User> friendList = DFS(friend, userSet);
                usersList.addAll(friendList);
            }
        });

        return usersList;
    }

    /**
     * finding the most sociable community
     * @return list - with all the users from the community with most related users
     */
    public List<Iterable<User>> mostSociableCommunity() throws RepositoryException {
        AtomicReference<List<Iterable<User>>> list = new AtomicReference<>(new ArrayList<>());
        Set<User> userSet = new HashSet<>();

        AtomicInteger max = new AtomicInteger(-1);
        userRepository.findAll().forEach(user -> {
            if (!userSet.contains(user)) {
                List<User> auxList = DFS(user, userSet); // finding all the related users
                int longestPath = longestPath(auxList); // finding the longest path in the graph
                if (longestPath > max.get()) {
                    list.set(new ArrayList<>());
                    list.get().add(auxList); // if a more sociable community is found add it to the final list
                    max.set(longestPath);
                } else if (longestPath == max.get())
                    list.get().add(auxList);
            }
        });

        return list.get();
    }

    /**
     * intermediary method to find the longest path in the graph formed by the related users
     * @param nodes - the users in the graph
     * @return max - the longest path
     */
    private int longestPath(List<User> nodes) {

        final int[] max = {0};

        nodes.forEach(user -> {
                int longestPath = longestPathFromSource(user);
                if (max[0] < longestPath)
                    max[0] = longestPath;
        });

        return max[0];
    }

    // this function is used to initialise the set
    private int longestPathFromSource(User source) {
        Set<User> set = new HashSet<>();
        return lee(source, set);
    }

    /**
     * searching for the longest path from a source using Lee algorithm
     * @param source - starting node for the source
     * @param set - saving the users in this set
     * @return max + 1 - the longest path
     */
    private int lee(User source, Set<User> set) {

        final int[] max = {-1};
        source.getFriends().forEach(friend -> {
            if(!set.contains(friend))
            {
                set.add(friend);
                int l = lee(friend, set);
                if(l > max[0])
                    max[0] = l;
                set.remove(friend);
            }
        });

        return max[0] + 1;
    }
}

package com.github.n8downs.ejenta_challenge;

import java.util.*;
import java.util.stream.*;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.StringUtils;

public class Party {
    private Set<String> people;
    private HashMap<String, HashSet<String>> friendships;
    private int friendshipsNecessary;
    private Set<String> toUninvite;

    public Party(Set<String> people, List<Pair<String, String>> friendshipsList, int friendshipsNecessary){
        this.people = people;
        this.friendshipsNecessary = friendshipsNecessary;
        this.toUninvite = new HashSet<String>();
    
        this.friendships = new HashMap<String, HashSet<String>>();
        for (Pair<String, String> pair : friendshipsList){
            addFriendship(pair.getLeft(), pair.getRight());
            addFriendship(pair.getRight(), pair.getLeft());
        }
    }

    private void addFriendship(String person, String friend){
        if (!friendships.containsKey(person)){
            friendships.put(person, new HashSet<String>());
        }
        friendships.get(person).add(friend);
    }

    private void uninvite(String person){
        if (toUninvite.contains(person)){
            return;
        }

        toUninvite.add(person);
        if (friendships.containsKey(person)){
            for (String friend : friendships.get(person)){
                if (!toUninvite.contains(friend)){
                    friendships.get(friend).remove(person);
                    if (friendships.get(friend).size() < friendshipsNecessary){
                        uninvite(friend);
                    }
                }
            }
        }
    }

    public Set<String> getInvites() {
        for (String person : people) {
            if (!friendships.containsKey(person) || friendships.get(person).size() < friendshipsNecessary){
                uninvite(person);
            }
        }


        return people.stream().filter(p -> !toUninvite.contains(p)).collect(Collectors.toCollection(HashSet::new));
    }
}

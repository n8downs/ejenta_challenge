package com.github.n8downs.ejenta_challenge;

import java.util.*;
import junit.framework.TestCase;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class PartyTest extends TestCase {
    private HashSet<String> createPeople(String... people){
        return new HashSet<String>(asList(people));
    }

    private ArrayList<Pair<String, String>> createFriendships(String... rawFriendships){
        ArrayList<Pair<String, String>> friendships = new ArrayList<Pair<String, String>>();
        for (String rawFriendship : rawFriendships){
            String[] parts = StringUtils.split(rawFriendship, ",");
            assertThat(String.format("friendship definitions must be: 'A,B', got: '%s'", rawFriendship),
                asList(parts), hasSize(2));
            friendships.add(Pair.of(parts[0], parts[1]));
        }
        return friendships;
    }

    public void test_two_people_who_are_friends() {
        Party party = new Party(
            createPeople("A", "B"),
            createFriendships("A,B"),
            1
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(2));
        assertThat(invites, containsInAnyOrder("A", "B"));
    }

    public void test_two_people_who_are_not_friends(){
        Party party = new Party(
            createPeople("A", "B"),
            createFriendships(),
            1
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(0));
    }

    public void test_people_under_the_friend_limit() {
        Party party = new Party(
            createPeople("A", "B"),
            createFriendships("A,B"),
            2
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(0));
    }

    public void test_three_people_who_are_all_friends(){
        Party party = new Party(
            createPeople("A", "B", "C"),
            createFriendships("A,B", "B,C", "C,A"),
            2
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(3));
        assertThat(invites, containsInAnyOrder("A", "B", "C"));
    }

    public void test_fourth_person_isnt_connected_enough(){
        Party party = new Party(
            createPeople("A", "B", "C", "D"),
            createFriendships("A,B", "B,C", "C,A", "C,D"),
            2
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(3));
        assertThat(invites, containsInAnyOrder("A", "B", "C"));
    }

    public void test_removing_the_fifth_person_makes_fourth_not_connected_enough(){
        Party party = new Party(
            createPeople("A", "B", "C", "D", "E"),
            createFriendships("A,B", "B,C", "C,A", "C,D", "D,E"),
            2
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(3));
        assertThat(invites, containsInAnyOrder("A", "B", "C"));
    }

    public void test_cycle_of_removals_is_not_infinite(){
        Party party = new Party(
            createPeople("A", "B", "C", "D"),
            createFriendships("A,B", "B,C", "C,D", "D,A"),
            3
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(0));
    }

    public void test_two_unlinked_cycles_are_both_invited(){
        Party party = new Party(
            createPeople("A", "B", "C", "D", "E", "F", "G"),
            createFriendships("A,B", "B,C", "C,A", "D,E", "E,F", "F,G", "G,D"),
            2
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(7));
        assertThat(invites, containsInAnyOrder("A", "B", "C", "D", "E", "F", "G"));
    }

    public void test_five_connections(){
        Party party = new Party(
            createPeople("A", "B", "C", "D", "E", "F"),
            createFriendships("A,B", "A,C", "A,D", "A,E", "A,F",
                                     "B,C", "B,D", "B,E", "B,F",
                                            "C,D", "C,E", "C,F",
                                                   "D,E", "D,F",
                                                          "E,F"),
            5
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(6));
        assertThat(invites, containsInAnyOrder("A", "B", "C", "D", "E", "F"));
    }

    public void test_almost_five_connections(){
        Party party = new Party(
            createPeople("A", "B", "C", "D", "E", "F"),
            createFriendships("A,B", "A,C", "A,D", "A,E", "A,F",
                                     "B,C", "B,D", "B,E", "B,F",
                                            "C,D", "C,E", "C,F",
                                                   "D,E", "D,F"),
            5
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(0));
    }

    public void test_binary_tree_four_levels_deep(){
        Party party = new Party(
            createPeople("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O"),
            createFriendships("A,B", "A,C",
                              "B,D", "B,E", "C,F", "C,G",
                              "D,H", "D,I", "E,J", "E,K", "F,L", "F,M", "G,N", "G,O"),
            2
        );

        Set<String> invites = party.getInvites();
        assertThat(invites, hasSize(0));
    }
}

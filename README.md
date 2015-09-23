# ejenta_challenge

This is a little bit of code I wrote for an engineering interview and to come a bit more up to speed since the last time I wrote Java (almost a decade ago in college).

The goal here was to come up with a decision on the most people we could invite to a party, given a list of people, a list of friendships, and the stipulation that guests must be friends with N other guests. Actually, the question hardcoded N=5, but 5 is a tough number to reason about and write simple tests.

My output is basically just a set of tests around an interface that should solve the above problem. Tests should look something like:

```java
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
```

To run the tests, simply:
```
~ $ brew install maven
~ $ git clone git@github.com:n8downs/ejenta_challenge.git
~ $ cd ejenta_challenge
~/ejenta_challenge $ mvn test
```

A few personal notes:
- I found the state of the Java world to be a bit easier to work with than I remember, though just as verbose.
- Once I spun up on Maven, it made dependencies quite easy to pull in, and build options quite easy to specify.
- It's weird that Java versions are now (6, 7, 8), but the output from (e.g.) java -version is "1.8.0_45."
- Hamcrest (weird name) provided some decent ways of testing generic collections.
  - Aside: I'm not sure I'm a fan of the ways test framework authors will bend over backwards to let you craft English sentences (e.g. assertThat(2, is(equalTo(1+1))))
- It was nice to use Java's new lambdas to filter a collection. Again, they feel a little clunky compared to other languages, but they're certainly a good tool to have in the toolbox!

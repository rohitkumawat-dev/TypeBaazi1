package com.typebaazi;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/** Local passage bank: one server-selected text shared by both racers. */
final class Paragraphs {
    private Paragraphs() {}

    // Original passages. No external API, download or key is needed.
    private static final List<String> PASSAGES = List.of(
        "The bicycle repair shop opened before sunrise. Its owner placed a small radio beside the tools and lifted a blue bicycle onto the stand. A loose chain had brought its rider to a stop on the way to college. While the repair was underway, the student watched each movement with interest. Ten minutes later, the wheels turned smoothly again. The owner explained how to check the chain at home, and the student left with a new skill as well as a working bicycle.",
        "At the edge of the forest, a narrow bridge crossed a quiet stream. Four friends stopped there during their morning walk to listen to the water. One noticed a tiny plant growing between two stones and wondered how its roots had found enough soil. They took a photograph without touching it and continued up the path. The walk had no particular destination. For once, getting somewhere mattered less than noticing what was already around them.",
        "The first version of the weather app showed sunshine during a thunderstorm. Nobody on the team could explain it until someone noticed that the city name was wrong. They corrected the request, added a useful error message, and tested it again. The next forecast matched the rain tapping against the window. That afternoon taught them a simple lesson about building software. Before making a system more complicated, check whether the basic information going into it is correct.",
        "The bakery window filled with warm light as the street outside began to wake. A delivery driver parked near the door, and the baker handed over a paper bag without being asked. They had followed the same routine for years. Today, a new assistant watched the exchange and wrote down the order. There was more to learn than recipes and oven temperatures. Remembering the people who returned each morning was also part of keeping the small business alive.",
        "The railway platform was crowded, but the little bookstall remained calm. A traveler had twenty minutes before departure and asked for something short to read. The seller chose a collection of stories and opened it to the first page. By the time the announcement sounded, the traveler was already halfway through the opening scene. A journey that had begun with a delayed connection now promised an unexpected adventure, even before the train had moved.",
        "A group of students turned an unused classroom into a small recording studio. Thick curtains softened the echoes, and a borrowed microphone stood in the center of the room. Their first recording included a ringing phone, a squeaking chair, and someone laughing in the corridor. Instead of giving up, they made a checklist and tried again. The final episode was only ten minutes long, but every clear sentence represented a problem they had learned to solve together.",
        "The old camera had spent years inside a cupboard. When Mira found it, she expected it to be broken, but the shutter still clicked. She carried it through the neighborhood and photographed ordinary things: a painted gate, a sleeping cat, and a bus waiting at a crossing. With only a few pictures available, each choice needed attention. By evening, the familiar streets seemed different. Looking carefully had made a routine walk feel like a new place.",
        "On the rooftop, the seedlings leaned toward the morning sun. The gardener turned each pot a little and checked the soil with a finger. Some plants needed water, while others needed another day to dry. A neighbor asked why the smallest plant was growing so slowly. The gardener smiled and pointed to its fresh leaves. Progress was happening, just at a different pace. The rooftop had become a useful reminder that healthy growth could not always be hurried.",
        "The final round of the quiz began with both teams on equal points. A difficult question appeared on the screen, and the room fell quiet. One player reached for the buzzer, paused, and asked a teammate to check the answer. Those few seconds made the difference. They answered correctly and won by a single point. Afterward, both teams stayed to discuss the questions. The competition was over, but the curiosity that had brought them there was still going strong.",
        "A long power cut changed the plans for the evening. Without a television or working chargers, the family gathered near the balcony with a board game. The rules were forgotten twice, and the youngest player insisted on reading them aloud. Soon the room was full of jokes and friendly arguments. When the lights finally returned, nobody moved to switch on the screen. An inconvenience had quietly become the most enjoyable part of their week.",
        "The hiking trail looked easy on the map, but the last climb was steeper than expected. The group slowed down and stopped at a shaded rock to drink water. From there, they could see the village far below and the path they had already covered. The summit was still some distance away. Breaking the remaining climb into smaller stretches made it feel manageable, and they reached the top with enough energy to enjoy the view.",
        "Inside the workshop, a wooden bird slowly took shape. The craftsperson made small cuts, checked the outline, and brushed the dust from the table. A visitor asked how long it took to finish one piece. The answer depended on the wood, the tools, and the mistakes that needed correcting. There was no button that could make the careful parts happen faster. The visitor stayed longer than planned, watching an ordinary block become something with character.",
        "The new bus route confused nearly everyone on its first morning. Passengers compared maps while the driver explained the changed stops. An elderly man worried about missing the hospital, so a student offered to remind him when they arrived. The ride continued through unfamiliar streets and slowly became less uncertain. By the end of the journey, several strangers had helped each other find their way. A different route had led to a small lesson in cooperation.",
        "Before the concert, the music room was full of unfinished sounds. A guitar needed tuning, the keyboard stand was uneven, and the drummer had misplaced a stick. The group solved each problem and began with a slow practice run. Their timing improved when they listened more carefully to one another. Later, the audience heard a smooth performance. Nobody outside the room knew how much of that confidence had been built through patient repetition earlier in the day.",
        "The science exhibition opened with a model bridge that could hold a surprising amount of weight. Its builders invited visitors to add coins one at a time. Each new load drew a larger crowd around the table. When the bridge finally bent, the students measured the result and wrote it down instead of hiding the failure. That number would guide their next design. The broken model had become useful evidence, and the experiment had done exactly what it was meant to do.",
        "The beach was almost empty when the cleanup began. Volunteers carried reusable bags and walked slowly along the shore, collecting plastic caught between stones. As the sun rose, more people joined them. A shop owner brought drinking water, and a child pointed out a bottle hidden under the sand. No single person could finish the whole job. Together, they cleared a stretch of coastline that looked noticeably different by the time the morning visitors arrived.",
        "A library card opened more possibilities than the new student expected. Beyond the shelves of novels were language recordings, old newspapers, and a collection of maps. The librarian explained how to search the catalog and reserve a book. The student arrived looking for one assignment reference and left with several new questions. A useful place does more than provide an answer. Sometimes it helps you discover what you want to learn next.",
        "The little cafe had no room for a large menu. Its owner offered a few sandwiches, fresh fruit, and coffee made carefully to order. At first, customers asked for more choices. Over time, they began returning for the reliable favorites. Keeping things simple made it easier to notice when something needed improvement. A better slice of bread or a warmer greeting could matter more than adding another page of items that nobody had time to prepare well.",
        "Two friends entered a typing race after arguing about who was faster. One began with a burst of speed and several mistakes. The other moved steadily, reading each phrase before typing it. As the clock counted down, both learned to balance speed with attention. The final result was close enough to demand another match. Winning felt good, but the real surprise was how much an ordinary keyboard could turn into a source of excitement.",
        "The community garden started with an empty patch of ground and a handwritten notice. A few residents brought tools, and others shared seeds from their own balconies. Nobody agreed on every detail, but they found room for different ideas. Flowers grew beside vegetables, and a narrow path connected the beds. Months later, the garden offered more than fresh produce. People who had barely spoken before now had a reason to stop, talk, and work together.",
        "The museum guide asked the visitors to choose one object and imagine the person who had used it. A worn compass caught a young visitor's attention. Its scratches suggested long journeys, although the display could only confirm a few details. The group compared their guesses with the written history. Imagination made the object interesting, while evidence kept the story honest. By the next room, everyone was looking more closely at the small details behind the glass.",
        "A rainstorm arrived just before the outdoor movie was due to begin. The organizers moved chairs under a covered courtyard and tested the projector against a plain wall. The screen was smaller than planned, but the audience helped arrange the space. Someone brought extra cushions, and another person found an extension cable. When the opening scene finally appeared, the crowd cheered. The evening worked because people adapted together instead of waiting for the original plan to become possible.",
        "The chess club welcomed its newest member with a friendly game. After a quick defeat, the beginner expected a list of mistakes. Instead, the opponent pointed out two good moves and explained one important turning point. They reset the pieces and played from that position again. Understanding the reason behind a move proved more useful than memorizing a sequence. By the end of the session, losing once had become the starting point for learning something lasting.",
        "The first delivery of the day contained a box with the wrong address. Rather than leaving it at the nearest door, the courier called the number on the label and checked the street name. The intended building was only a short distance away. The recipient had been waiting for a replacement part to finish a repair. A careful check that took less than a minute saved both people a much longer delay later that afternoon."
    );

    // Bounded, server-local memory of each player's most recent opening.
    // It prevents immediate repeats even when other rooms start in between.
    private static final Map<String, Integer> LAST_OPENING =
        new LinkedHashMap<>(128, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Integer> entry) {
                return size() > 10000;
            }
        };

    static synchronized String choose(String... playerIds) {
        Set<Integer> excluded = new HashSet<>();
        for (String id : playerIds) {
            if (id != null && LAST_OPENING.containsKey(id)) {
                excluded.add(LAST_OPENING.get(id));
            }
        }
        List<Integer> openings = new ArrayList<>();
        for (int i = 0; i < PASSAGES.size(); i++) {
            if (!excluded.contains(i)) openings.add(i);
        }
        // Defensive fallback if this utility is reused with a very large group.
        if (openings.isEmpty()) {
            for (int i = 0; i < PASSAGES.size(); i++) openings.add(i);
        }
        int first = openings.get(ThreadLocalRandom.current().nextInt(openings.size()));
        List<Integer> remaining = new ArrayList<>();
        for (int i = 0; i < PASSAGES.size(); i++) {
            if (i != first) remaining.add(i);
        }
        Collections.shuffle(remaining);
        List<String> selected = new ArrayList<>();
        selected.add(PASSAGES.get(first));
        int words = wordCount(PASSAGES.get(first));
        for (int index : remaining) {
            if (words >= 700) break;
            selected.add(PASSAGES.get(index));
            words += wordCount(PASSAGES.get(index));
        }
        for (String id : playerIds) {
            if (id != null) LAST_OPENING.put(id, first);
        }
        return String.join(" ", selected);
    }

    private static int wordCount(String text) {
        return text.split("\\s+").length;
    }
}

package com.typebaazi;
import java.util.*;
final class Paragraphs {
 private Paragraphs() {}
 private static final List<String> PASSAGES=List.of(
  "The train reached the station just as the rain began. People hurried under the shelter while a tea seller carefully moved his cups away from the edge. A student opened a book and waited for the next announcement. Across the platform, two old friends recognized each other after many years. Their conversation made the long delay feel surprisingly short.",
  "A small team spent Saturday building a game in a quiet classroom. The first version refused to start, and the second version forgot the score. They laughed, shared a packet of biscuits, and tried again. By evening, the screen finally displayed a winner. The best part was not the result but the moment everyone realized that their idea had become something real.",
  "The market woke before the rest of the town. Fresh flowers filled one corner, and the smell of warm bread drifted from a nearby shop. A cyclist rang his bell as he passed a stack of empty boxes. At the fruit stall, a little girl counted coins carefully before choosing three bright oranges to take home for her family.",
  "Learning a new skill rarely follows a straight line. There are days when progress feels quick and others when a simple task seems impossible. A short break can help more than another hour of frustration. Notice what went wrong, change one thing, and try again. Small improvements become visible when you compare your work with where you started rather than with someone else.",
  "Beyond the last streetlight, the sky looked larger than anyone expected. The group spread a blanket on the grass and searched for familiar stars. Someone brought a notebook to draw the patterns they could find. A cool breeze moved through the trees, and the noise of the city slowly faded. For a while, nobody felt the need to check a phone.",
  "The library had a window overlooking a busy road. Inside, pages turned quietly and chairs scraped gently against the floor. A new visitor asked for a story about the sea. The librarian returned with three books and a smile. Choosing just one proved difficult, so the visitor borrowed all three and planned a weekend filled with distant islands and unexpected adventures.",
  "On the first morning of the trip, everyone woke earlier than planned. Bags were packed, tickets were checked, and one missing charger caused a brief search. The bus arrived with minutes to spare. As the buildings gave way to fields, the group began sharing stories. By the next stop, even the quietest person had joined the conversation and suggested a song.",
  "A good competition brings out effort without taking away the fun. Some players begin quickly, while others build a steady rhythm as they go. The clock gives everyone the same opportunity, but attention makes a difference. Read carefully, keep moving, and correct a mistake when you notice it. Whatever the final score, there is always another chance to challenge a friend."
 );
 static String choose() {
  List<String> parts=new ArrayList<>(PASSAGES);
  Collections.shuffle(parts);
  return String.join(" ",parts);
 }
}

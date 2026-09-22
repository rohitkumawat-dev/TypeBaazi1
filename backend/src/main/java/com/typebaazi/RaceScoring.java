package com.typebaazi;
final class RaceScoring {
 record Score(int correct,double wpm,double accuracy,int mistakes) {}
 static Score score(String target,String text,int seconds) {
  int correct=0;
  for(int i=0;i<Math.min(target.length(),text.length());i++)
   if(target.charAt(i)==text.charAt(i)) correct++;
  return new Score(correct,round(correct*12.0/seconds),
   text.isEmpty()?0:round(correct*100.0/text.length()),text.length()-correct);
 }
 private static double round(double value){return Math.round(value*10)/10.0;}
}

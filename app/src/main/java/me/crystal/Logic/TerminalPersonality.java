package me.crystal.Logic;

import java.util.Random;

public class TerminalPersonality {

    private static final Random random = new Random();
    private static PersonalityState currentPersonality = PersonalityState.NEUTRAL;

    public static String getRemark() {
        // Force a mood change each time a remark is needed
        changeMood();

        String remark = switch (currentPersonality) {
            case NEUTRAL -> neutralRemarks();
            case SNARKY -> snarkyRemarks();
            case HELPFUL -> helpfulRemarks();
            case ANNOYED -> annoyedRemarks();
            case SARCASTIC -> sarcasticRemarks();
            case CONFUSED -> confusedRemarks();
        };
        return "Comment from Terminal: " + remark; // Added prefix here
    }

    private static void changeMood() {
        PersonalityState[] moods = PersonalityState.values();
        currentPersonality = moods[random.nextInt(moods.length)];
    }

    private static String neutralRemarks() {
        return "Hmm... Interesting.";
    }

    private static String snarkyRemarks() {
        String[] remarks = {
                "Really? How original...",
                "Wow, didn't see that coming...",
                "Another day, another yawn."
        };
        return remarks[random.nextInt(remarks.length)];
    }

    private static String helpfulRemarks() {
        String[] remarks = {
                "Good job!",
                "You're getting the hang of it.",
                "That was a smart move!"
        };
        return remarks[random.nextInt(remarks.length)];
    }

    private static String annoyedRemarks() {
        String[] remarks = {
                "Ugh, not again...",
                "Can you please stop?",
                "It's getting on my nerves."
        };
        return remarks[random.nextInt(remarks.length)];
    }

    private static String sarcasticRemarks() {
        String[] remarks = {
                "Oh sure, like that's going to work...",
                "Brilliant! *eye roll*",
                "Because it's never gone wrong before, right?"
        };
        return remarks[random.nextInt(remarks.length)];
    }

    private static String confusedRemarks() {
        String[] remarks = {
                "Wait, what? I'm confused...",
                "Uh... okay?",
                "Not sure what you're trying to achieve."
        };
        return remarks[random.nextInt(remarks.length)];
    }

    private enum PersonalityState {
        NEUTRAL,
        SNARKY,
        HELPFUL,
        ANNOYED,
        SARCASTIC,
        CONFUSED
    }
}
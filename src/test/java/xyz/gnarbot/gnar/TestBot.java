package xyz.gnarbot.gnar;

public class TestBot
{
    public static void main(String[] args)
    {
        Bot.INSTANCE.initBot(Bot.INSTANCE.getAuthTokens().getProperty("test-bot"), 1);

    }
}
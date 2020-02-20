package TaterTodt;

import simple.hooks.scripts.task.Task;
import simple.robot.api.ClientContext;

public class EatTask extends Task {

    public EatTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return ctx.players.getLocal().getHealth() <= Main.EAT_THRESHOLD; //for now we don't run this
    }

    @Override
    public void run() {
        Main.state = Main.State.EATING;
        if (ctx.inventory.populate().filter("Lobster").population() > 0) {
            ctx.inventory.populate().filter("Lobster").next().click("Eat");
        } else {
            System.out.println("Out of food, idling in safe area until healed or game end.");
            ctx.pathing.step(Main.SAFE_TILE);
        }
    }

    @Override
    public String status() {
        return "Eating food";
    }
}


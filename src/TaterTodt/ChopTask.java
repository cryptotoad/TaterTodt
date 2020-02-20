package TaterTodt;

import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

public class ChopTask extends Task {

    public ChopTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        ctx.inventory.populate();
        return !ctx.inventory.inventoryFull();// Checks if our inventory is full or not, if not call the run method
    }

    @Override
    public void run() {
        ctx.inventory.populate();
        Main.state = Main.State.CHOPPING;
        if (ctx.getPlayers().getLocal().getAnimation() == -1) { // Checks if we're currently woodcutting(animating) right now, if not jump to body
            SimpleObject bruma = ctx.getObjects().populate().filter("Bruma roots").nextNearest(); // Grabs the nearest 'Willow' tree in the game
            if (bruma != null && bruma.validateInteractable()) { // Checks if the tree isn't null, if it isn't it will turn and walk to it
                bruma.click(0);
                ctx.onCondition(() -> ctx.getPlayers().getLocal().getAnimation() != -1 || !ctx.inventory.inventoryFull(), 5000);// Checks if we're chopping with a 5 second time-out, so we don't spam click 'Chop down'
            }
        }
    }

    @Override
    public String status() {
        return "Chopping bruma roots";
    }

}
package TaterTodt;

import simple.hooks.scripts.task.Task;
import simple.robot.api.ClientContext;

public class FletchTask extends Task {

    public FletchTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        ctx.inventory.populate();
        //If we're full of bruma roots, or we have both roots and kindling in our inventory then we fletch
        return (ctx.inventory.populate().filter("Bruma root").population() > 0 && ctx.inventory.inventoryFull())
                || (ctx.inventory.populate().filter("Bruma root").population() > 0 && ctx.inventory.populate().filter("Bruma kindling").population() > 0);
    }

    @Override
    public void run() {
        ctx.inventory.populate();
        Main.state = Main.State.FLETCHING;
        if (ctx.inventory.populate().filter("Bruma root").population() > 0 &&
                ctx.getPlayers().getLocal().getAnimation() == -1  && Main.interrupted)
        {
            try {
                ctx.inventory.populate().filter("Knife").next().click(0);
                ctx.inventory.populate().filter("Bruma root").next().click(0);
            } catch(java.lang.NullPointerException ex) {
                ex.printStackTrace();
            }
        }
        if(ctx.players.getLocal().getAnimation() != -1)
            Main.interrupted = true;
    }

    @Override
    public String status() {
        return "Fletching";
    }
}


package TaterTodt;

import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

public class FeedTask extends Task {


    public FeedTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        ctx.inventory.populate();
        //If we're full of bruma kindling, or we have only kindling in our inventory then we feed
        return (ctx.inventory.filter("Bruma kindling").population() > 0 && ctx.inventory.inventoryFull())
                || (ctx.inventory.filter("Bruma kindling").population() > 0 && !(ctx.inventory.filter("Bruma root").population() > 0));    }

    @Override
    public void run() {
        if (ctx.objects.populate().filter("Brazier").population() > 0) {
            try {
                ctx.pathing.step(ctx.objects.populate().filter("Brazier").nextNearest().getLocation());
                Main.state = Main.State.LIGHTING_BRAZIER;
                SimpleObject unlit_brazier = ctx.objects.populate().filter("Brazier").nextNearest();
                unlit_brazier.validateInteractable();
                unlit_brazier.click("Light");
                ctx.sleep(1200);
            } catch(java.lang.NullPointerException ex) {
                ex.printStackTrace();
            }
        } else{
            if( Main.interrupted) {
                Main.state = Main.State.FEEDING;
                Main.interrupted = false;
                SimpleObject burning_brazier = ctx.objects.populate().filter("Burning brazier").nextNearest();
                if(burning_brazier.validateInteractable())
                    burning_brazier.click(0);
            }
        }
    }

    //the cold of the wintertodt seeps into your bones.
    @Override
    public String status() {
        return "Feeding brazier";
    }
}


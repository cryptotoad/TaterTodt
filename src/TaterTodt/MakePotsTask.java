package TaterTodt;

import simple.hooks.scripts.task.Task;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.api.ClientContext;

import static TaterTodt.Main.VIEW_TILE;

public class MakePotsTask extends Task {


    public MakePotsTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        return ctx.inventory.populate().filter("Rejuvenation potion (4)","Rejuvenation potion (3)",
                "Rejuvenation potion (2)","Rejuvenation potion (1)").population() == 0;
    }

    private void makeRoom(){
        if(ctx.inventory.getFreeSlots() < 1) {
            //If we have no room, we need to drop shit.
            if(ctx.inventory.populate().filter("Bruma root").population() >= 2) {
                ctx.inventory.populate().filter("Bruma root").next().click("Drop");
            } else if(ctx.inventory.populate().filter("Bruma kindling").population() >= 2) {
                ctx.inventory.populate().filter("Bruma kindling").next().click("Drop");
            }
        }
    }

    @Override
    public void run()
    {
        Main.state = Main.State.MAKING_POTS;
        //If we already have an unfinished pot we don't need more
        if (ctx.inventory.populate().filter("Rejuvenation potion (unf)").population() > 0)
        {
            if(ctx.inventory.populate().filter("Bruma herb").population() > 0) { //We have both ingredients, make a potion
                ctx.inventory.populate().filter("Bruma herb").next().click(0);
                ctx.inventory.populate().filter("Rejuvenation potion (unf)").next().click(0);
            } else { //If we need an herb
                makeRoom();

                if (ctx.getPlayers().getLocal().getAnimation() == -1) { // Checks if we're currently woodcutting(animating) right now, if not jump to body
                    SimpleObject sprouting_roots = ctx.getObjects().populate().filter("Sprouting Roots").nextNearest(); // Grabs the nearest 'Willow' tree in the game
                    if (sprouting_roots != null && sprouting_roots.validateInteractable()) { // Checks if the tree isn't null, if it isn't it will turn and walk to it
                        sprouting_roots.click(0);
                        ctx.sleep(1200);
                    } else {
                        ctx.pathing.step(VIEW_TILE);
                    }
                }
            }
        } else { //If we need a potion
            makeRoom();
            ctx.pathing.step(Main.SAFE_TILE);
            SimpleObject pot_crate = ctx.getObjects().populate().filter(29320).nextNearest(); // Grabs the nearest 'Willow' tree in the game
            if (pot_crate != null && pot_crate.validateInteractable()) { // Checks if the tree isn't null, if it isn't it will turn and walk to it
                pot_crate.click(0);
                ctx.sleep(1200);
            }
        }
    }

    //the cold of the wintertodt seeps into your bones.
    @Override
    public String status() {
        return "Making Potions";
    }
}
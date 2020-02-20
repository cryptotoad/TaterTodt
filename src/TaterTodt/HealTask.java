package TaterTodt;

import simple.hooks.scripts.task.Task;
import simple.robot.api.ClientContext;

public class HealTask extends Task {


    public HealTask(ClientContext ctx) {
        super(ctx);
    }

    @Override
    public boolean condition() {
        if(ctx.inventory.populate().filter("Rejuvenation potion (4)","Rejuvenation potion (3)","Rejuvenation potion (2)","Rejuvenation potion (1)").population() == 0) {
            return false; //can't heal, why bother trying
        }
        if(ctx.npcs.populate().filter("Pyromancer").nearest().next() == null) {
            return true;
        }
        if(ctx.npcs.populate().filter("Incapacitated Pyromancer").nearest().next() == null) {
            return false;
        }
        return ctx.players.getLocal().getLocation().distanceTo(ctx.npcs.populate().filter("Incapacitated Pyromancer")
                .nearest().next().getLocation()) < ctx.players.getLocal().getLocation().distanceTo(ctx.npcs.populate()
                    .filter("Pyromancer").nearest().next().getLocation());
    }

    @Override
    public void run() {
        Main.state = Main.State.HEALING;
        //If we're closer to an incapacitated pyromancer than a live one, we heal him
        if (ctx.npcs.populate().filter("Incapacitated Pyromancer").population() > 0 && ctx.npcs.populate().filter("Incapacitated Pyromancer").nearest().next() != null)
        {
            if(ctx.inventory.populate().filter("Rejuvenation potion (4)","Rejuvenation potion (3)",
                    "Rejuvenation potion (2)","Rejuvenation potion (1)").population() > 0)
            {
                try {
                    //If we already have a potion then we don't need to make one
                    ctx.inventory.populate().filter("Rejuvenation potion (4)", "Rejuvenation potion (3)",
                            "Rejuvenation potion (2)", "Rejuvenation potion (1)").next().click(0);
                    ctx.npcs.populate().filter("Incapacitated Pyromancer").nearest().next().turnTo();
                    ctx.npcs.populate().filter("Incapacitated Pyromancer").nearest().next().click(0);
                    ctx.sleep(600);
                } catch(java.lang.NullPointerException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    //the cold of the wintertodt seeps into your bones.
    @Override
    public String status() {
        return "Feeding brazier";
    }
}
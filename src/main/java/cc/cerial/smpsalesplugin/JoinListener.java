package cc.cerial.smpsalesplugin;

import com.destroystokyo.paper.ParticleBuilder;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.util.Vector;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class JoinListener implements Listener {
    private static final Map<Player, Byte> colorMap = new HashMap<>();
    private static final SecureRandom random = new SecureRandom();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        colorMap.put(player, (byte) (random.nextInt(7) + 1));
        byte color = colorMap.get(player);

        Color realColor = Color.fromRGB(getColorFromNum(color));
        Location spiralLoc = player.getLocation().clone();
        spiralLoc.setPitch(-90);
        spiralLoc.setYaw(0);
        spiralLoc.add(new Vector(0, -0.5, 0));
        drawSpiral(realColor, spiralLoc, 1, 8 , 0.001, 50);
    }

    private int getColorFromNum(byte color) {
        return switch (color) {
            case 1 -> 0x0000AA;
            case 2 -> 0x00AA00;
            case 3 -> 0x00AAAA;
            case 4 -> 0xAA0000;
            case 5 -> 0xAA00AA;
            case 6 -> 0xFFAA00;
            case 7 -> 0xAAAAAA;
            case 8 -> 0x555555;
            default -> 0;
        };
    }

    private Vector sphericalVector(double radius, double theta, double phi) {
        double r = Math.abs(radius);
        double t = Math.toRadians(theta);
        double p = Math.toRadians(phi);
        double sinp = Math.sin(p);
        double x = r * sinp * Math.cos(t);
        double y = r * Math.cos(p);
        double z = r * sinp * Math.sin(t);
        return new Vector(x, y, z);
    }

    private float properYaw(float yaw) {
        return yaw > 270 ? yaw - 270 : yaw + 90;
    }


    private void drawSpiral(Color color, Location loc, double size, double distance, double m, long delay) {
        ParticleBuilder builder = new ParticleBuilder(Particle.DUST)
                .color(color, 1)
                .extra(0);

        new Thread(() -> {
            for (int i = 0; i < ((360 * size * distance) / 6); i++) {
                Vector vSphere = sphericalVector(
                        size,
                        properYaw(i * 6),
                        90
                );
                Location l = loc.clone()
                        .add(loc.getDirection().multiply(m * i * 6))
                        .add(vSphere);

                builder
                        .allPlayers()
                        .location(l)
                        .spawn();

                if (i % 60 == 0) {
                    try {
                        Thread.sleep(delay);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}

package dev.arubik.mctl.utils;

import java.util.HashMap;
import java.util.Optional;

import org.bukkit.entity.Player;

import dev.arubik.mctl.enums.mood;
import dev.arubik.mctl.utils.MultiThings.BiObject;
import dev.arubik.mctl.utils.MultiThings.MultiObjects.TriObject;
import dev.arubik.mctl.utils.TripleMap.TripleMap;

public class TestingThings {
    public static TripleMap<String, Player, mood> m = new TripleMap<String, Player, mood>();

    public static BiObject<Player, mood> bil = new BiObject<Player, mood>();

    public static void a(Player p, mood a) {
        m.put("test", p, a);

        m.forEach(key -> {
            Player p2 = m.getv(key);
            mood a2 = m.getc(key);
        });
        m.forEachAlls((key, val1, val2) -> {
            String key2 = key;
            Player p2 = val1;
            mood a2 = val2;
        });

        BiObject<TriObject<BiObject<Player, mood>, BiObject<Player, mood>, BiObject<Player, mood>>, BiObject<Player, mood>> b = new BiObject<TriObject<BiObject<Player, mood>, BiObject<Player, mood>, BiObject<Player, mood>>, BiObject<Player, mood>>();
    }

}

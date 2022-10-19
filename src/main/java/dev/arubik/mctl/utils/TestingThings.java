package dev.arubik.mctl.utils;

import java.util.HashMap;
import java.util.Optional;

import org.bukkit.entity.Player;

import dev.arubik.mctl.enums.Mood;
import dev.arubik.mctl.utils.MultiThings.BiObject;
import dev.arubik.mctl.utils.MultiThings.MultiObjects.TriObject;
import dev.arubik.mctl.utils.TripleMap.TripleMap;

public class TestingThings {
    public static TripleMap<String, Player, Mood> m = new TripleMap<String, Player, Mood>();

    public static BiObject<Player, Mood> bil = new BiObject<Player, Mood>();

    public static void a(Player p, Mood a) {
        m.put("test", p, a);

        m.forEach(key -> {
            Player p2 = m.getv(key);
            Mood a2 = m.getc(key);
        });
        m.forEachAlls((key, val1, val2) -> {
            String key2 = key;
            Player p2 = val1;
            Mood a2 = val2;
        });

        BiObject<TriObject<BiObject<Player, Mood>, BiObject<Player, Mood>, BiObject<Player, Mood>>, BiObject<Player, Mood>> b = new BiObject<TriObject<BiObject<Player, Mood>, BiObject<Player, Mood>, BiObject<Player, Mood>>, BiObject<Player, Mood>>();
    }

}

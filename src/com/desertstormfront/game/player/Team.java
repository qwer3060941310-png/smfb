/*
 * Decompiled with CFR 0.152.
 */
package com.desertstormfront.game.player;

public strictfp enum Team {
    TeamA("TeamA[i18n]: Team A", "TEAM_A"),
    TeamB("TeamB[i18n]: Team B", "TEAM_B"),
    TeamC("TeamC[i18n]: Team C", "TEAM_C"),
    TeamD("TeamD[i18n]: Team D", "TEAM_D");

    private String e;
    private String f;

    /*
     * WARNING - void declaration
     */
    private Team(String var3_1, String var4_2) {
        this.e = var3_1;
        this.f = var4_2;
    }

    public String getName() {
        return this.e;
    }

    public String getKey() {
        return this.f;
    }

    public static Team fromKey(String string) {
        if (string.equals("N/A")) {
            return null;
        }
        int i1 = 0;
        while (i1 < Team.values().length) {
            Team team = Team.values()[i1];
            if (team.getKey().equals(string)) {
                return team;
            }
            ++i1;
        }
        return null;
    }

    public String toString() {
        return this.e;
    }
}


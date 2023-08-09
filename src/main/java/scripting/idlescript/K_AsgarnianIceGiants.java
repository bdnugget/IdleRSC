package scripting.idlescript;

import java.awt.GridLayout;
import javax.swing.*;
import orsc.ORSCharacter;

/**
 * <b>Ice Dungeon Ice Giant/Warrior Killer</b><br>
 *
 * <p>This bot supports the "autostart" Parameter"); Usage: foodname numberOfFood potUp? example:
 * "shark,5,true". "autostart": uses lobsters,5,true. <br>
 * Start in Fally East bank or In Ice Cave. <br>
 * Food in bank REQUIRED. <br>
 * Use regular Atk/Str Pots option. <br>
 * Food Withdraw amount <br>
 * Type of Food <br>
 *
 * @see scripting.idlescript.K_kailaScript
 * @author Kaila
 */
public final class K_AsgarnianIceGiants extends K_kailaScript {
  private static final int[] loot = {
    UNID_GUAM, // Grimy Guam
    UNID_MAR, // Grimy Marrentill
    UNID_TAR, // Grimy Tarromin
    UNID_HAR, // Grimy Harralander
    UNID_RANARR, // Grimy Ranarr Weed
    UNID_IRIT, // Grimy Irit
    UNID_AVANTOE, // Grimy Avantoe
    UNID_KWUARM, // Grimy Kwuarm
    UNID_CADA, // Grimy Cadantine
    UNID_DWARF, // Grimy Dwarf Weed
    AIR_RUNE, // air rune
    EARTH_RUNE, // earth runF
    FIRE_RUNE,
    WATER_RUNE,
    NATURE_RUNE, // nature rune
    LAW_RUNE, // law rune
    BODY_RUNE, // body rune  //remove
    COSMIC_RUNE, // cosmic rune
    MIND_RUNE, // remove
    CHAOS_RUNE, // chaos rune
    DEATH_RUNE, // Death Rune
    BLOOD_RUNE, // blood rune
    COINS, // coins
    153, // mithril ore
    11, // bronze arrow
    433, // Black Kite
    126, // Mithril Square
    BIG_BONES, // Big bones
    BONES, // bones
    UNCUT_SAPP, // saph
    UNCUT_EMER, // emerald
    UNCUT_RUBY, // ruby
    UNCUT_DIA, // diamond
    TOOTH_HALF, // tooth half
    LOOP_HALF, // loop half
    LEFT_HALF, // shield (left) half
    RUNE_SPEAR // rune spear
  };

  private void startSequence() {
    c.displayMessage("@red@Asgarnian Ice Giants - By Kaila");
    c.displayMessage("@red@Start in Fally East bank with Armor");
    c.displayMessage("@red@Sharks IN BANK REQUIRED");
    startTime = System.currentTimeMillis();
    if (c.isInBank()) c.closeBank();
    if (c.currentY() < 3000) {
      bank();
      BankToIce();
      c.sleep(1380);
    }
  } // param 0 - type of food, param 1 - number of food, param 2 - potUp

  public int start(String[] parameters) {
    centerX = 308;
    centerY = 3520;
    centerDistance = 15;
    if (parameters[0].toLowerCase().startsWith("auto")) {
      c.displayMessage("Got Autostart, using 2 Lobs, yes pots", 0);
      System.out.println("Got Autostart, using 2 Lobs, yes pots");
      foodId = 373;
      lootBones = true;
      foodWithdrawAmount = 2;
      potUp = true;
      guiSetup = true;
      scriptStarted = true;
    }
    if (!guiSetup) {
      setupGUI();
      guiSetup = true;
    }
    if (scriptStarted) {
      guiSetup = false;
      scriptStarted = false;
      startSequence();
      scriptStart();
    }
    return 1000; // start() must return an int value now.
  }

  private void scriptStart() {
    while (c.isRunning()) {
      boolean ate = eatFood();
      if (!ate) {
        c.setStatus("@red@We've ran out of Food! Running Away!.");
        IceToBank();
        bank();
        BankToIce();
      }
      buryBones(false);
      checkFightMode();
      checkInventoryItemCounts();
      if (c.getInventoryItemCount() < 30) {
        lootItems(true, loot);
        if (lootBones) lootItem(true, BIG_BONES);
        if (potUp) {
          attackBoost(0, true);
          strengthBoost(0, true);
        }
        if (!c.isInCombat()) {
          int[] npcIds = {135, 158};
          ORSCharacter npc = c.getNearestNpcByIds(npcIds, false);
          if (npc != null) {
            c.setStatus("@yel@Attacking..");
            // c.walktoNPC(npc.serverIndex,1);
            c.attackNpc(npc.serverIndex);
            c.sleep(GAME_TICK);
          } else {
            c.sleep(GAME_TICK);
            lootItems(true, loot);
            if (lootBones) lootItem(true, BIG_BONES);
            // if (c.currentX() != 305 || c.currentY() != 3522) {
            //  c.walkTo(305, 3522);
            //  c.sleep(1000);
            // }
          }
        } else c.sleep(GAME_TICK);
      }
      if (c.getInventoryItemCount() == 30) {
        dropItemToLoot(false, 1, EMPTY_VIAL);
        buryBonesToLoot(false);
      }
      if (c.getInventoryItemCount() == 30 || c.getInventoryItemCount() == 0 || timeToBank) {
        c.setStatus("@yel@Banking..");
        IceToBank();
        bank();
        BankToIce();
      }
    }
  }

  private void bank() {
    c.setStatus("@yel@Banking..");
    c.openBank();
    c.sleep(640);
    if (!c.isInBank()) {
      waitForBankOpen();
    } else {
      totalGuam = totalGuam + c.getInventoryItemCount(165);
      totalMar = totalMar + c.getInventoryItemCount(435);
      totalTar = totalTar + c.getInventoryItemCount(436);
      totalHar = totalHar + c.getInventoryItemCount(437);
      totalRan = totalRan + c.getInventoryItemCount(438);
      totalIrit = totalIrit + c.getInventoryItemCount(439);
      totalAva = totalAva + c.getInventoryItemCount(440);
      totalKwuarm = totalKwuarm + c.getInventoryItemCount(441);
      totalCada = totalCada + c.getInventoryItemCount(442);
      totalDwarf = totalDwarf + c.getInventoryItemCount(443);
      totalLaw = totalLaw + c.getInventoryItemCount(42);
      totalNat = totalNat + c.getInventoryItemCount(40);
      totalDeath = totalDeath + c.getInventoryItemCount(38);
      totalBlood = totalBlood + c.getInventoryItemCount(619);
      totalLoop = totalLoop + c.getInventoryItemCount(527);
      totalTooth = totalTooth + c.getInventoryItemCount(526);
      totalLeft = totalLeft + c.getInventoryItemCount(1277);
      totalGems =
          totalGems
              + c.getInventoryItemCount(160)
              + c.getInventoryItemCount(159)
              + c.getInventoryItemCount(158)
              + c.getInventoryItemCount(157);
      totalSpear = totalSpear + c.getInventoryItemCount(1092);
      if (c.getInventoryItemCount() > 2) {
        for (int itemId : c.getInventoryItemIds()) {
          if (itemId != 476
              && itemId != 475
              && itemId != 224
              && itemId != 223) { // dont deposit partial potions!
            c.depositItem(itemId, c.getInventoryItemCount(itemId));
          }
        }
      }
      c.sleep(1280);
      if (potUp) {
        withdrawAttack(1);
        withdrawStrength(1);
      }
      withdrawItem(foodId, foodWithdrawAmount);
      bankItemCheck(foodId, 30);
      c.closeBank();
      checkInventoryItemCounts();
    }
  }

  private void IceToBank() {
    c.setStatus("@gre@Walking to Bank..");
    c.walkTo(302, 3519);
    c.walkTo(293, 3519);
    c.walkTo(291, 3521);
    c.walkTo(284, 3522);
    c.walkTo(279, 3531);
    c.walkTo(279, 3540);
    c.walkTo(285, 3544);
    c.atObject(285, 3543); // go up ladder
    c.sleep(1000);
    c.walkTo(287, 711);
    c.walkTo(287, 694);
    c.walkTo(287, 680);
    c.walkTo(287, 673);
    c.walkTo(287, 665);
    c.walkTo(287, 652);
    c.walkTo(289, 650);
    c.walkTo(288, 649);
    c.walkTo(288, 639);
    c.walkTo(288, 629);
    c.walkTo(288, 619);
    c.walkTo(288, 609);
    c.walkTo(290, 607);
    c.walkTo(290, 597);
    c.walkTo(290, 587);
    c.walkTo(290, 577);
    c.walkTo(290, 575);
    c.walkTo(286, 571);
    c.sleep(640);
    totalTrips = totalTrips + 1;
    c.setStatus("@gre@Done Walking..");
  }

  private void BankToIce() {
    c.setStatus("@gre@Walking to Ice Dungeon..");
    c.walkTo(287, 571);
    c.walkTo(290, 575);
    c.walkTo(290, 577);
    c.walkTo(290, 587);
    c.walkTo(290, 597);
    c.walkTo(290, 607);
    c.walkTo(288, 609);
    c.walkTo(288, 619);
    c.walkTo(288, 629);
    c.walkTo(288, 639);
    c.walkTo(288, 649);
    c.walkTo(289, 650);
    c.walkTo(287, 652);
    c.walkTo(287, 665);
    // add pathing??
    c.walkTo(287, 673);
    c.walkTo(287, 680);
    c.walkTo(287, 694);
    c.walkTo(287, 711);
    c.walkTo(285, 712);
    c.atObject(285, 711); // go down ladder
    c.sleep(1000);
    // c.walkTo(282,3543);
    c.walkTo(285, 3544);
    c.walkTo(279, 3540);
    c.walkTo(279, 3531);
    c.walkTo(284, 3522);
    c.walkTo(291, 3521);
    c.walkTo(293, 3519);
    c.walkTo(302, 3519);
    c.walkTo(305, 3522);
    c.setStatus("@gre@Done Walking..");
  }
  // GUI stuff below (icky)
  private void setupGUI() {
    JLabel header = new JLabel("Ice Dungeon Ice Giant/Warrior Killer - by Kaila");
    JLabel label1 = new JLabel("Start in Fally East bank or In Ice Cave");
    JLabel spacer = new JLabel("             ");
    JLabel label2 = new JLabel("Chat commands can be used to direct the bot");
    JLabel label3 = new JLabel("Example ::bank ");
    JLabel label4 = new JLabel("Combat Styles ::attack :strength ::defense ::controller");
    JLabel label5 = new JLabel("This bot supports the \"autostart\" Parameter");
    JLabel label6 = new JLabel("Usage: foodname numberOfFood potUp?");
    JLabel label7 = new JLabel("example: \"shark,5,true\"");
    JLabel label8 = new JLabel("\"autostart\": uses lobsters,5,true");
    JCheckBox potUpCheckbox = new JCheckBox("Use regular Atk/Str Pots?", true);
    JLabel foodWithdrawAmountLabel = new JLabel("Food Withdraw amount:");
    JTextField foodWithdrawAmountField = new JTextField(String.valueOf(6));
    JLabel foodLabel = new JLabel("Type of Food:");
    JComboBox<String> foodField = new JComboBox<>(foodTypes);
    foodField.setSelectedIndex(2); // sets default to sharks
    JLabel fightModeLabel = new JLabel("Fight Mode:");
    JComboBox<String> fightModeField =
        new JComboBox<>(new String[] {"Controlled", "Aggressive", "Accurate", "Defensive"});
    JLabel blankLabel = new JLabel("          ");
    JButton startScriptButton = new JButton("Start");

    startScriptButton.addActionListener(
        e -> {
          foodId = foodIds[foodField.getSelectedIndex()];
          if (!foodWithdrawAmountField.getText().equals("")) {
            foodWithdrawAmount = Integer.parseInt(foodWithdrawAmountField.getText());
          } else if (foodWithdrawAmountField.getText().equals("")) {
            foodWithdrawAmount = 1;
          }
          potUp = potUpCheckbox.isSelected();
          scriptFrame.setVisible(false);
          scriptFrame.dispose();
          scriptStarted = true;
        });

    scriptFrame = new JFrame(c.getPlayerName() + " - options");

    scriptFrame.setLayout(new GridLayout(0, 1));
    scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    scriptFrame.add(header);
    scriptFrame.add(label1);
    scriptFrame.add(label2);
    scriptFrame.add(spacer);
    scriptFrame.add(label3);
    scriptFrame.add(label4);
    scriptFrame.add(spacer);
    scriptFrame.add(label5);
    scriptFrame.add(label6);
    scriptFrame.add(label7);
    scriptFrame.add(label8);
    scriptFrame.add(spacer);
    scriptFrame.add(potUpCheckbox);
    scriptFrame.add(foodWithdrawAmountLabel);
    scriptFrame.add(foodWithdrawAmountField);
    scriptFrame.add(foodLabel);
    scriptFrame.add(foodField);
    scriptFrame.add(fightModeLabel);
    scriptFrame.add(fightModeField);
    scriptFrame.add(blankLabel);
    scriptFrame.add(startScriptButton);

    scriptFrame.pack();
    scriptFrame.setLocationRelativeTo(null);
    scriptFrame.setVisible(true);
    scriptFrame.requestFocusInWindow();
  }

  @Override
  public void chatCommandInterrupt(String commandText) {
    if (commandText.contains("bank")) {
      c.displayMessage("@red@Got banking command! Going to the Bank!");
      timeToBank = true;
      c.sleep(100);
    } else if (commandText.contains(
        "attack")) { // field is "Controlled", "Aggressive", "Accurate", "Defensive"}
      c.displayMessage("@red@Got Combat Style Command! - Attack Xp");
      c.displayMessage("@red@Switching to \"Accurate\" combat style!");
      fightMode = 2;
      c.sleep(100);
    } else if (commandText.contains("strength")) {
      c.displayMessage("@red@Got Combat Style Command! - Strength Xp");
      c.displayMessage("@red@Switching to \"Aggressive\" combat style!");
      fightMode = 1;
      c.sleep(100);
    } else if (commandText.contains("defense")) {
      c.displayMessage("@red@Got Combat Style Command! - Defense Xp");
      c.displayMessage("@red@Switching to \"Defensive\" combat style!");
      fightMode = 3;
      c.sleep(100);
    } else if (commandText.contains("controlled")) {
      c.displayMessage("@red@Got Combat Style Command! - Controlled Xp");
      c.displayMessage("@red@Switching to \"Controlled\" combat style!");
      fightMode = 0;
      c.sleep(100);
    }
  }

  @Override
  public void paintInterrupt() {
    if (c != null) {
      String runTime = c.msToString(System.currentTimeMillis() - startTime);
      int guamSuccessPerHr = 0;
      int marSuccessPerHr = 0;
      int tarSuccessPerHr = 0;
      int harSuccessPerHr = 0;
      int ranSuccessPerHr = 0;
      int iritSuccessPerHr = 0;
      int avaSuccessPerHr = 0;
      int kwuSuccessPerHr = 0;
      int cadaSuccessPerHr = 0;
      int dwarSuccessPerHr = 0;
      int lawSuccessPerHr = 0;
      int natSuccessPerHr = 0;
      int deathSuccessPerHr = 0;
      int bloodSuccessPerHr = 0;
      int GemsSuccessPerHr = 0;
      int TripSuccessPerHr = 0;
      long currentTimeInSeconds = System.currentTimeMillis() / 1000L;

      try {
        float timeRan = currentTimeInSeconds - startTimestamp;
        float scale = (60 * 60) / timeRan;
        guamSuccessPerHr = (int) ((totalGuam + inventGuam) * scale);
        marSuccessPerHr = (int) ((totalMar + inventMar) * scale);
        tarSuccessPerHr = (int) ((totalTar + inventTar) * scale);
        harSuccessPerHr = (int) ((totalHar + inventHar) * scale);
        ranSuccessPerHr = (int) ((totalRan + inventRan) * scale);
        iritSuccessPerHr = (int) ((totalIrit + inventIrit) * scale);
        avaSuccessPerHr = (int) ((totalAva + inventAva) * scale);
        kwuSuccessPerHr = (int) ((totalKwuarm + inventKwuarm) * scale);
        cadaSuccessPerHr = (int) ((totalCada + inventCada) * scale);
        dwarSuccessPerHr = (int) ((totalDwarf + inventDwarf) * scale);
        lawSuccessPerHr = (int) ((totalLaw + inventLaws) * scale);
        natSuccessPerHr = (int) ((totalNat + inventNats) * scale);
        GemsSuccessPerHr = (int) ((totalGems + inventGems) * scale);
        deathSuccessPerHr = (int) ((totalDeath + inventDeath) * scale);
        bloodSuccessPerHr = (int) ((totalBlood + inventBlood) * scale);
        TripSuccessPerHr = (int) (totalTrips * scale);
      } catch (Exception e) {
        // divide by zero
      }
      c.drawString("@red@Asgarnian Ice Slayer @mag@~ by Kaila", 330, 48, 0xFFFFFF, 1);
      c.drawString(
          "@whi@Guams: @gre@"
              + (totalGuam + inventGuam)
              + "@yel@ (@whi@"
              + String.format("%,d", guamSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          62,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Marrentills: @gre@"
              + (totalMar + inventMar)
              + "@yel@ (@whi@"
              + String.format("%,d", marSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          76,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Tarromins: @gre@"
              + (totalTar + inventTar)
              + "@yel@ (@whi@"
              + String.format("%,d", tarSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          90,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Harralanders: @gre@"
              + (totalHar + inventHar)
              + "@yel@ (@whi@"
              + String.format("%,d", harSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          104,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Ranarrs: @gre@"
              + (totalRan + inventRan)
              + "@yel@ (@whi@"
              + String.format("%,d", ranSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          118,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Irit Herbs: @gre@"
              + (totalIrit + inventIrit)
              + "@yel@ (@whi@"
              + String.format("%,d", iritSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          132,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Avantoes: @gre@"
              + (totalAva + inventAva)
              + "@yel@ (@whi@"
              + String.format("%,d", avaSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          146,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Kwuarms: @gre@"
              + (totalKwuarm + inventKwuarm)
              + "@yel@ (@whi@"
              + String.format("%,d", kwuSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          160,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Cadantines: @gre@"
              + (totalCada + inventCada)
              + "@yel@ (@whi@"
              + String.format("%,d", cadaSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          174,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Dwarfs: @gre@"
              + (totalDwarf + inventDwarf)
              + "@yel@ (@whi@"
              + String.format("%,d", dwarSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          188,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Laws: @gre@"
              + (totalLaw + inventLaws)
              + "@yel@ (@whi@"
              + String.format("%,d", lawSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          202,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Nats: @gre@"
              + (totalNat + inventNats)
              + "@yel@ (@whi@"
              + String.format("%,d", natSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          216,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Deaths: @gre@"
              + (totalDeath + inventDeath)
              + "@yel@ (@whi@"
              + String.format("%,d", deathSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          230,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Bloods: @gre@"
              + (totalBlood + inventBlood)
              + "@yel@ (@whi@"
              + String.format("%,d", bloodSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          244,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Gems: @gre@"
              + (totalGems + inventGems)
              + "@yel@ (@whi@"
              + String.format("%,d", GemsSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          258,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Tooth: @gre@"
              + (totalTooth + inventTooth)
              + "@yel@ / @whi@Loop: @gre@"
              + (totalLoop + inventLoop),
          350,
          272,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@R.Spear: @gre@"
              + (totalSpear + inventSpear)
              + "@yel@ / @whi@Shield Half: @gre@"
              + (totalLeft + inventLeft),
          350,
          286,
          0xFFFFFF,
          1);
      c.drawString(
          "@whi@Total Trips: @gre@"
              + totalTrips
              + "@yel@ (@whi@"
              + String.format("%,d", TripSuccessPerHr)
              + "@yel@/@whi@hr@yel@)",
          350,
          300,
          0xFFFFFF,
          1);
      c.drawString("@whi@Runtime: " + runTime, 350, 314, 0xFFFFFF, 1);
    }
  }
}

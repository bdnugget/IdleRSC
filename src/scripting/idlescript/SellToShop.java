package scripting.idlescript;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class SellToShop extends IdleScript {
	JFrame scriptFrame = null;
	boolean guiSetup = false;
	boolean scriptStarted = false;
	int[] itemIds = {};
	int[] npcId = {};
	int shopNumber = -1;
	int startX = -1;
	int startY = -1;
	JTextField items = new JTextField("");
	JTextField shopCount = new JTextField("10");
	JTextField vendorId = new JTextField("51,55,87,105,145,168,185,222,391,82,83,88,106,146,169,186,223");

	public void start(String parameters[]) {
		startX = controller.currentX();
		startY = controller.currentZ();
		if (!guiSetup) {
			setupGUI();
			guiSetup = true;
		}
		while (scriptStarted && controller.isRunning()) {
			scriptStart();
		}
	}

	public void startWalking(int x, int y) {
		// shitty autowalk
		int newX = x;
		int newY = y;
		while (controller.currentX() != x || controller.currentZ() != y) {
			if (controller.currentX() - x > 23) {
				newX = controller.currentX() - 20;
			}
			if (controller.currentZ() - y > 23) {
				newY = controller.currentZ() - 20;
			}
			if (controller.currentX() - x < -23) {
				newX = controller.currentX() + 20;
			}
			if (controller.currentZ() - y < -23) {
				newY = controller.currentZ() + 20;
			}
			if (Math.abs(controller.currentX() - x) <= 23) {
				newX = x;
			}
			if (Math.abs(controller.currentZ() - y) <= 23) {
				newY = y;
			}
			if (!controller.isTileEmpty(newX, newY)) {
				controller.walkToAsync(newX, newY, 2);
				controller.sleep(640);
			} else {
				controller.walkToAsync(newX, newY, 0);
				controller.sleep(640);
			}
		}
	}

	public boolean isSellable(int id) {
		for (int i = 0; i < itemIds.length; i++) {
			if (itemIds[i] == id)
				return true;
		}

		return false;
	}

	public void scriptStart() {
		while(controller.getNearestNpcByIds(npcId,false) == null) {
			startWalking(startX,startY);
		}
		while (controller.getNearestNpcByIds(npcId, false) != null && !controller.isInShop()) {
			controller.npcCommand1(controller.getNearestNpcByIds(npcId, false).serverIndex);
			controller.sleep(640);
		}
		while (controller.isInShop() && controller.getInventoryItemCount() > 1 || controller.isInShop()
				&& controller.getInventoryItemCount(10) < 1 && controller.getInventoryItemCount() == 1) {
			for (int itemId : itemIds) {
				if (itemId != 0 && isSellable(itemId) && controller.shopItemCount(itemId) < shopNumber) {
					controller.shopSell(itemId, shopNumber - controller.getBankItemCount(itemId));
					controller.sleep(640);
				}
			}
		}
		while (controller.getInventoryItemCount() == 1 && controller.getInventoryItemCount(10) > 0
				|| controller.getInventoryItemCount() == 0) {
			startWalking(controller.getNearestBank()[0], controller.getNearestBank()[1]);
			while (controller.getNearestNpcById(95, false) == null) {
				startWalking(controller.getNearestBank()[0], controller.getNearestBank()[1]);
			}
			while (!controller.isInBank()) {
				controller.openBank();
				controller.sleep(430);
			}
			if (controller.isInBank()) {
				controller.depositItem(10, controller.getInventoryItemCount(10));
				for (int itemId : itemIds) {
					if (itemId != 0 && isSellable(itemId) && controller.shopItemCount(itemId) < shopNumber) {
						controller.withdrawItem(itemId, 30);
						controller.sleep(640);
					}
				}
			}
		}
		if (!controller.isLoggedIn()) {
			controller.sleep(1000);
		}
		if (!controller.isRunning()) {
			guiSetup = false;
		}
	}

	public static void centerWindow(Window frame) {
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
		frame.setLocation(x, y);
	}

	public void completeSetup() {
		if (items.getText().contains(",")) {
			for (String value : items.getText().replace(" ", "").split(",")) {
				this.itemIds = Arrays.copyOf(itemIds, itemIds.length + 1);
				this.itemIds[itemIds.length - 1] = Integer.parseInt(value);
			}
		} else {
			this.itemIds = new int[] { Integer.parseInt(items.getText()) };
		}
		if (vendorId.getText().contains(",")) {
			for (String value : vendorId.getText().replace(" ", "").split(",")) {
				this.npcId = Arrays.copyOf(npcId, npcId.length + 1);
				this.npcId[npcId.length - 1] = Integer.parseInt(value);
			}
		} else {
			this.npcId = new int[] { Integer.parseInt(vendorId.getText()) };
		}
		shopNumber = Integer.parseInt(shopCount.getText());
	}

	public void setupGUI() {
		JLabel header = new JLabel("Sell To Shop");
		JButton startScriptButton = new JButton("Start");
		JLabel itemsLabel = new JLabel("Item Ids to sell");
		JLabel shopCountLabel = new JLabel("Max number the shop can already have");
		JLabel vendorIdLabel = new JLabel("Shopkeeper ids");

		startScriptButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scriptFrame.setVisible(false);
				scriptFrame.dispose();
				scriptStarted = true;
				controller.displayMessage("@gre@" + '"' + "heh" + '"' + " - Searos");
				completeSetup();
				controller.displayMessage("@red@SelltoShop started");

			}
		});

		scriptFrame = new JFrame("Script Options");

		scriptFrame.setLayout(new GridLayout(0, 1));
		scriptFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		scriptFrame.add(header);
		scriptFrame.add(vendorIdLabel);
		scriptFrame.add(vendorId);
		scriptFrame.add(itemsLabel);
		scriptFrame.add(items);
		scriptFrame.add(shopCountLabel);
		scriptFrame.add(shopCount);
		scriptFrame.add(startScriptButton);
		centerWindow(scriptFrame);
		scriptFrame.setVisible(true);
		scriptFrame.pack();
		scriptFrame.requestFocus();
	}
}
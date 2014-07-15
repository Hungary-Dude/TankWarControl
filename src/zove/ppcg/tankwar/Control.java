/**
 * 
 */
package zove.ppcg.tankwar;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import zove.ppcg.tankwar.TurnAction.ActionType;
import zove.ppcg.tankwar.object.TankObject;

/**
 * Control program for the tank war
 * 
 * @author Zove Games
 */
public class Control {
	private static Set<Class<? extends Tank>> tankClasses = new HashSet<>();
	private static List<Tank> tanks;
	private static List<TankObject> tankObjects;
	private static Map<String, Integer> scores = new HashMap<>();
	protected static Logger logger = Logger.getLogger("Control");
	protected static JFrame frame;
	protected static GuiPanel panel;
	protected static int round = 0;
	protected static Battlefield field;

	// ////// Parameters, change as necessary for your tests
	public static final boolean hasGui = true;
	public static final long sleepTime = 100;
	public static final int NUM_ROUNDS = 1;
	public static final int FIELD_SIZE = 20;
	public static final Level logLevel = Level.OFF;

	/**
	 * Entry point
	 * 
	 * @param args
	 * @throws Exception
	 *             On rainy days
	 */
	public static void main(String[] args) throws Exception {
		logger.setLevel(logLevel);
		logger.info("Loading tanks...");

		BufferedReader in = new BufferedReader(new InputStreamReader(
				new FileInputStream("tanks.list")));
		String str;
		while ((str = in.readLine()) != null) {
			Class<?> tankClass = Class.forName(str);
			tankClasses.add(tankClass.asSubclass(Tank.class));
		}
		in.close();

		logger.info("Starting game...");

		if (hasGui)
			createGUI();

		tanks = new ArrayList<>();
		for (Class<? extends Tank> tankClass : tankClasses) {
			logger.info("\tFound tank class: " + tankClass.getName());
			tanks.add(tankClass.newInstance());
		}
		for (int i = 0; i < NUM_ROUNDS; i++) {
			round++;
			logger.info("Round " + (i + 1));
			field = new Battlefield();
			// probably could happen in extreme cases

			tankObjects = new ArrayList<>();

			for (Tank tank : tanks) {
				MapPoint point = field.emptyPoints.get((int) Math.floor(Math
						.random() * field.emptyPoints.size()));
				field.emptyPoints.remove(point);
				TankObject obj = new TankObject(tank, point);
				field.field[point.getX()][point.getY()] = obj;

				tankObjects.add(obj);
				tank.onSpawn(field, point);
				if (scores.get(tank.getName()) == null)
					scores.put(tank.getName(), 0);
			}
			logger.info("Round " + (i + 1) + " running");

			long startTime = System.currentTimeMillis();

			List<TankObject> deadTanks = new LinkedList<TankObject>();

			while (tankObjects.size() > 1) {
				if (System.currentTimeMillis() - startTime > 2000) {
					// logger.info("\t" + tankObjects.size() + " tanks left");
					startTime = System.currentTimeMillis();
				}

				for (TankObject tank : tankObjects) {
					TurnAction turn = tank.getTank().onTurn(field,
							tank.getPosition(), tank.getHealth());
					if (turn == null)
						turn = TurnAction.createNothingAction();
					turn.takeAction(tank, field);
					if (hasGui && turn.type == ActionType.SHOOT) {
						synchronized (panel.shots) {
							panel.shots
									.add(new MapPoint[] {
											tank.getPosition(),
											tank.getPosition()
													.cloneAndTranslate(
															(int) (Battlefield.DIAGONAL_FIELD_SIZE * Math
																	.cos(turn.angle)),
															(int) (Battlefield.DIAGONAL_FIELD_SIZE * Math
																	.sin(turn.angle))),
											new MapPoint(2, 2), turn.hit });
						}
					}
				}
				if (hasGui) {
					panel.mapString = field.toString();
					frame.repaint();
				}

				// see who died this iteration
				// clear tanks from last iteration, so we know exactly who
				// tied later
				deadTanks.clear();
				List<TankObject> removeTankObjects = new ArrayList<>();
				for (TankObject obj : tankObjects) {
					if (obj.getHealth() < 0) {
						removeTankObjects.add(obj);
						deadTanks.add(obj);
					}
				}
				tankObjects.removeAll(removeTankObjects);

				if (sleepTime > 0)
					Thread.sleep(sleepTime);
			}
			if (tankObjects.size() > 0) {
				// one winner
				Tank survivor = tankObjects.get(0).getTank();
				tankObjects.remove(0);
				logger.info(survivor.getName() + " survived!");
				survivor.onDestroyed(field, true);
				scores.put(survivor.getName(),
						scores.get(survivor.getName()) + 1);
			} else if (deadTanks.size() >= 2) {
				// simultaneous kills - it's a tie
				logger.info("It's a tie between "
						+ Arrays.toString(deadTanks.toArray()) + "!");
				for(TankObject o: deadTanks){
					scores.put(o.getTank().getName(),
							scores.get(o.getTank().getName()) + 1);
				}
			} else {
				// can only happen when there's only one tank
				// I did say results are undefined
			}

			if (hasGui) {
				synchronized (panel.shots) {
					panel.shots.clear();
				}
			}
			field = null;
			tankObjects = null;
			System.gc();
		}

		System.out.println("Scores:");

		Map<Integer, LinkedList<String>> scores2 = new TreeMap<>();

		int maxStringLength = 0;

		for (String s : scores.keySet()) {
			if (s.length() > maxStringLength)
				maxStringLength = s.length();
			int score = scores.get(s);
			if (scores2.get(score) == null)
				scores2.put(score, new LinkedList<String>());
			scores2.get(score).add(s);
		}

		for (int i = 0; i < maxStringLength + 8; i++) {
			System.out.print("-");
		}
		System.out.println();
		for (Integer i : scores2.keySet()) {
			LinkedList<String> list = scores2.get(i);
			Collections.sort(list);
			for (String s : list) {
				System.out.print("| ");
				System.out.print(s);
				for (int i2 = 0; i2 < maxStringLength - s.length(); i2++)
					System.out.print(" ");
				System.out.print(" | ");
				System.out.print(i);
				System.out.println(" |");
			}
		}

		for (int i = 0; i < maxStringLength + 8; i++) {
			System.out.print("-");
		}

		System.exit(0);
	}

	private static void createGUI() {
		frame = new JFrame("Control GUI");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel = new GuiPanel());
		panel.setPreferredSize(new Dimension(500, 500));
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	private static class GuiPanel extends JPanel {
		private static final long serialVersionUID = -9102446479715765885L;
		volatile String mapString = new String(new byte[Battlefield.FIELD_SIZE
				* Battlefield.FIELD_SIZE]);

		ArrayList<MapPoint[]> shots = new ArrayList<>();

		BufferedImage b;
		Graphics2D g, gg;

		public GuiPanel() {
			b = new BufferedImage(500, 500, BufferedImage.TYPE_INT_ARGB);
			g = b.createGraphics();
			gg = (Graphics2D) g.create();
			gg.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			gg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
					RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		}

		public void paint(Graphics g2) {
			g.setColor(Color.WHITE);
			g.clearRect(0, 0, 500, 500);
			float s = 500f / Battlefield.FIELD_SIZE;
			for (int x = 0; x < Battlefield.FIELD_SIZE; x++) {
				for (int y = 0; y < Battlefield.FIELD_SIZE; y++) {
					char character = mapString
							.charAt((x * Battlefield.FIELD_SIZE) + y);
					float x2 = x, y2 = y;

					y2 = Battlefield.FIELD_SIZE - y2 - 1;
					Color c;
					switch (character) {
					case '#':
						c = Color.BLUE;
						break;
					case 'T':
						c = Color.GREEN;
						break;
					case 'R':
						c = Color.DARK_GRAY;
						break;
					case 'W':
						c = Color.ORANGE;
						break;
					default:
						c = Color.WHITE;
						break;
					}
					g.setColor(c);
					g.fillRect((int) (x2 * s), (int) (y2 * s), (int) s, (int) s);
				}
			}
			gg.setColor(Color.RED);
			synchronized (shots) {
				List<MapPoint[]> remove = new ArrayList<MapPoint[]>();
				for (MapPoint[] p : shots) {
					p[2] = p[2].cloneAndTranslate(-1, -1);
					if (p[2].getX() <= 0)
						remove.add(p);
				}
				shots.removeAll(remove);
				for (MapPoint[] p : shots) {
					float x1 = p[0].getX(), y1 = p[0].getY(), x2 = p[1].getX(), y2 = p[1]
							.getY();
					y1 = Battlefield.FIELD_SIZE - y1 - 1;
					y2 = Battlefield.FIELD_SIZE - y2 - 1;
					x1 += 0.5;
					y1 += 0.5;
					x2 += 0.5;
					y2 += 0.5;
					x1 *= s;
					y1 *= s;
					x2 *= s;
					y2 *= s;
					gg.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
					if (p[3] != null) {
						float x3 = p[3].getX(), y3 = p[3].getY();
						y3 = Battlefield.FIELD_SIZE - y3 - 1;
						g.setColor(Color.RED);
						g.fillRect((int) (x3 * s), (int) (y3 * s), (int) s,
								(int) s);
					}
				}
			}
			gg.setColor(Color.red);
			gg.setFont(new Font(Font.MONOSPACED, Font.BOLD, 24));
			gg.drawString("Round: " + Control.round, 0, 500);
			g2.drawImage(b, 0, 0, null);
		}
	}
}

package SatConn;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;
import java.awt.*;
import java.util.ArrayList;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import java.io.InputStream;

import static java.lang.Math.*;
import static SatConn.Clones.*;
import static SatConn.Check.*;
import static SatConn.Cnf.*;
import static SatConn.Connectivity.*;
import static SatConn.Relations.*;
import static SatConn.Math.*;

/**
 * A graphical tool to draw the solution graph of Boolean formulas and
 * relations, highlighted on an orthographic hypercube projection.
 *
 * @author Konrad W. Schwerdtfeger
 */
public class Tool extends javax.swing.JFrame {

	Panel pan = new Panel();
	boolean[] r;
	Color bgc;
	StringBuilder s;
	P2[] ces = new P2[16];
	ArrayList<Character> vs;
	Rectangle re = new Rectangle();
	P2 p = new P2(), q = new P2();

	static float wx = 0.95f, wy = 0.98f, ww = 20;
	static double pg = 3.5, gw = 1.3, gww = 1.3, pw = 4, pww = 3.2;
	static Color gr = new Color(0, 170, 0), gra = new Color(165, 165, 165);

	class Panel extends JPanel {
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			if (r != null) {
				float m = min(jPanel1.getHeight() * wx, jPanel1.getWidth() * wy);
				drwFnc(vectors(r), (Graphics2D) g, 0, ww, m, 0, 0, 0, bgc);
				g.setColor(new Color(0, 102, 204));
				g.setFont(new Font("Sans Serif", 0, 12));
				final int l = 53;
				g.drawString("save", jPanel1.getWidth() - l + 2, jPanel1.getHeight() - 30);
				g.drawString("graphics", jPanel1.getWidth() - l - 10, jPanel1.getHeight() - 16);
				g.setColor(Color.GRAY);
				g.drawRect(jPanel1.getWidth() - l - 15, jPanel1.getHeight() - 43, 57, 34);
				re = new Rectangle(jPanel1.getWidth() - l - 15, jPanel1.getHeight() - 43, 57, 34);
			}
		}
	}

	public Tool() {
		initComponents();
		jPanel1.add(pan);
		bgc = jPanel1.getBackground();
		ces[0] = new P2(100, 0);
		ces[1] = new P2(50, -50);
		ces[2] = new P2(0, -100);
		ces[3] = new P2(10, -24);
		ces[4] = new P2(19, -10);
		ces[5] = new P2(6, -3);
		jtf.requestFocusInWindow();
	}

	// <editor-fold defaultstate="collapsed" desc="Generated
	// <editor-fold defaultstate="collapsed" desc="Generated Code">                          
	private void initComponents() {

		jScrollPane1 = new javax.swing.JScrollPane();
		jtvec = new javax.swing.JTextArea();
		jPanel1 = new javax.swing.JPanel();
		jScrollPane2 = new javax.swing.JScrollPane();
		jtinv = new javax.swing.JTextArea();
		jLabel1 = new javax.swing.JLabel();
		jLabel2 = new javax.swing.JLabel();
		jScrollPane3 = new javax.swing.JScrollPane();
		jtf = new javax.swing.JTextArea();
		jLabel3 = new javax.swing.JLabel();
		jlB = new javax.swing.JLabel();
		jlS = new javax.swing.JLabel();
		jlC = new javax.swing.JLabel();
		jlcb = new javax.swing.JLabel();
		jButton1 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("SatConn");
		setBounds(new java.awt.Rectangle(10, 10, 10, 10));
		setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		jtvec.setColumns(20);
		jtvec.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
		jtvec.setLineWrap(true);
		jtvec.setRows(2);
		jtvec.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				jtvecKeyTyped(evt);
			}
		});
		jScrollPane1.setViewportView(jtvec);

		jPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jPanel1MouseClicked(evt);
			}
		});
		jPanel1.setLayout(new java.awt.BorderLayout());

		jtinv.setColumns(20);
		jtinv.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
		jtinv.setLineWrap(true);
		jtinv.setRows(2);
		jtinv.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				jtinvKeyTyped(evt);
			}
		});
		jScrollPane2.setViewportView(jtinv);

		jLabel1.setText("Vectors");
		jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		jLabel2.setText("Inverse");

		jtf.setColumns(20);
		jtf.setFont(new java.awt.Font("Consolas", 0, 13)); // NOI18N
		jtf.setLineWrap(true);
		jtf.setRows(2);
		jtf.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyTyped(java.awt.event.KeyEvent evt) {
				jtfKeyTyped(evt);
			}
		});
		jScrollPane3.setViewportView(jtf);

		jLabel3.setText("Formula");
		jLabel3.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

		jButton1.setForeground(new java.awt.Color(0, 0, 255));
		jButton1.setText("Help");
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(layout.createSequentialGroup()
										.addContainerGap()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jScrollPane2)
														.addComponent(jScrollPane3)
														.addComponent(jScrollPane1)
														.addGroup(layout.createSequentialGroup()
																		.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(jlcb)
																						.addComponent(jLabel1)
																						.addComponent(jLabel2)
																						.addGroup(layout.createSequentialGroup()
																										.addComponent(jlB, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																										.addComponent(jlS, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
																										.addGap(18, 18, 18)
																										.addComponent(jlC)))
																		.addGap(0, 87, Short.MAX_VALUE))
														.addGroup(layout.createSequentialGroup()
																		.addComponent(jLabel3)
																		.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
																		.addComponent(jButton1)))
										.addContainerGap())
		);
		layout.setVerticalGroup(
						layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
										.addContainerGap()
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel1)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
										.addComponent(jLabel2)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(18, 18, 18)
										.addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(jlB)
														.addComponent(jlS)
														.addComponent(jlC))
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jlcb)
										.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE))
		);

		pack();
	}// </editor-fold>                        

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
		if (h == null) {
			h = new ToolHelp();
		}
		h.setLocation(this.getBounds().x + this.getBounds().width, this.getBounds().y);
		h.setVisible(true);
  }//GEN-LAST:event_jButton1ActionPerformed
	ToolHelp h;

	private void jtvecKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jtvecKeyTyped
		iv();
		jtvec.setBackground(Color.WHITE);
		if (evt.getKeyChar() == '\n') {
			ev();
			String t = jtvec.getText();
			t = t.replace("\n", "");
			int p = jtvec.getCaretPosition();
			jtvec.setText(t);
			jtvec.setCaretPosition(p - 1);
			r = rel(t);
			jtinv.setText(r == null ? "Invalid relation" : string(invFnc(r)));
			jtf.setText(r == null ? "Invalid relation" : cnf(r, false));
			jtinv.setBackground(Color.WHITE);
			jtf.setBackground(Color.WHITE);
			clc();
			pan.repaint();
		}
	}// GEN-LAST:event_jtvecKeyTyped

	private void jtinvKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jtinvKeyTyped
		iv();
		jtinv.setBackground(Color.WHITE);
		if (evt.getKeyChar() == '\n') {
			ev();
			String t = jtinv.getText();
			t = t.replace("\n", "");
			int p = jtinv.getCaretPosition();
			jtinv.setText(t);
			jtinv.setCaretPosition(p - 1);
			r = rel(t);
			r = invFnc(r);
			jtvec.setText(r == null ? "Invalid relation" : string(r));
			jtf.setText(r == null ? "Invalid relation" : cnf(r, false));
			jtvec.setBackground(Color.WHITE);
			jtf.setBackground(Color.WHITE);
			clc();
			pan.repaint();
		}
	}// GEN-LAST:event_jtinvKeyTyped

	private void jtfKeyTyped(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_jtfKeyTyped
		iv();
		jtf.setBackground(Color.WHITE);
		if (evt.getKeyChar() == '\n') {
			String t = jtf.getText();
			t = t.replace("\n", "");
			int p = jtf.getCaretPosition();
			jtf.setText(t);
			jtf.setCaretPosition(p - 1);
			r = rel(jtf.getText());
			vs = (ArrayList<Character>) Parse.vs.clone();
			jtvec.setText(r == null ? "Invalid formula" : string(r));
			jtinv.setText(r == null ? "Invalid formula" : string(invFnc(r)));
			jtinv.setBackground(Color.WHITE);
			jtvec.setBackground(Color.WHITE);
			clc();
			pan.repaint();
		}
	}// GEN-LAST:event_jtfKeyTyped

	private void jPanel1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jPanel1MouseClicked
		if (r == null) {
			return;
		}
		float m = min(jPanel1.getHeight() * wx, jPanel1.getWidth() * wy);
		int c = drwFnc(vectors(r), (Graphics2D) jPanel1.getGraphics(), 0, ww, m, evt.getX(), evt.getY(), 0, bgc);
		if (c >= 0) {
			r[c] = !r[c];
			jtvec.setText(string(r));
			jtinv.setText(string(invFnc(r)));
			jtf.setText(cnf(r, false));
			iv();
			jtf.setBackground(Color.WHITE);
			jtinv.setBackground(Color.WHITE);
			jtvec.setBackground(Color.WHITE);
			clc();
			pan.repaint();
		}
		else if (re.contains(evt.getX(), evt.getY())) {
			JFileChooser fc = new JFileChooser() {
				@Override
				public void approveSelection() {
					File f = getSelectedFile();
					if (f.exists() && getDialogType() == SAVE_DIALOG) {
						int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file",
										JOptionPane.YES_NO_CANCEL_OPTION);
						switch (result) {
							case JOptionPane.YES_OPTION:
								super.approveSelection();
								return;
							case JOptionPane.NO_OPTION:
								return;
							case JOptionPane.CLOSED_OPTION:
								return;
							case JOptionPane.CANCEL_OPTION:
								cancelSelection();
								return;
						}
					}
					super.approveSelection();
				}
			};

			fc.setSelectedFile(new File("graph.svg"));
			if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
				File f = fc.getSelectedFile();
				try {
					PrintWriter w = new PrintWriter(f);
					w.println(s.toString());
					w.close();
				} catch (FileNotFoundException ex) {
					Logger.getLogger(Tool.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
		}
	}// GEN-LAST:event_jPanel1MouseClicked

	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(Tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Tool.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		// </editor-fold>

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new Tool().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify                     
	private javax.swing.JButton jButton1;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JLabel jLabel2;
	private javax.swing.JLabel jLabel3;
	private javax.swing.JPanel jPanel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JScrollPane jScrollPane2;
	private javax.swing.JScrollPane jScrollPane3;
	private javax.swing.JLabel jlB;
	private javax.swing.JLabel jlC;
	private javax.swing.JLabel jlS;
	private javax.swing.JLabel jlcb;
	private javax.swing.JTextArea jtf;
	private javax.swing.JTextArea jtinv;
	private javax.swing.JTextArea jtvec;
	// End of variables declaration                   

	void clc() {
		if (r != null) {
			if (r.length > 1 && r.length <= e2(4)) {
				jlB.setEnabled(true);
				jlS.setEnabled(true);
				jlB.setText("Clone: " + Clone(r));
				jlS.setText("Co-Clone: " + coClone(r));
			}
			else {
				jlB.setText("Clone: ");
				jlS.setText("Co-Clone: ");
			}
			jlC.setText("Components: " + comps(r).size());
			String gr = "rgb(0,140,0)";
			jlcb.setEnabled(true);
			jlC.setEnabled(true);
			jlcb.setText("<html>"
							+ "<font color=\"" + (cpss(r) ? gr : "gray") + "\">CPSS </font>&nbsp"
							+ "<font color=\"" + (schaefer(r) ? gr : "gray") + "\">Schaefer </font>"
							+ "<font color=\"" + (stight(r) ? gr : "gray") + "\">s.tight  </font>&nbsp&nbsp&nbsp"
							+ "<font color=\"" + (bij(r) ? gr : "gray") + "\">bijunctive </font>&nbsp"
							+ "<font color=\"" + (scbj(r) ? gr : "gray") + "\">s.c.bijunctive </font><br> "
							+ "<font color=\"" + (im(r) ? gr : "gray") + "\">IHSB- </font>&nbsp"
							+ "<font color=\"" + (scim(r) ? gr : "gray") + "\">s.c.IHSB- </font>&nbsp"
							+ "<font color=\"" + (ip(r) ? gr : "gray") + "\">IHSB+ </font>&nbsp"
							+ "<font color=\"" + (scip(r) ? gr : "gray") + "\">s.c.IHSB+ </font>&nbsp"
							+ "<font color=\"" + (horn(r) ? gr : "gray") + "\">Horn </font>&nbsp"
							+ "<font color=\"" + (dhorn(r) ? gr : "gray") + "\">d.Horn </font><br>"
							+ "<font color=\"" + (affine(r) ? gr : "gray") + "\">affine </font>&nbsp"
							+ "<font color=\"" + (sorfree(r) ? gr : "gray") + "\">s.OR-free </font>&nbsp"
							+ "<font color=\"" + (snfree(r) ? gr : "gray") + "\">s.NAND-free </font>");
		}
	}

	void iv() {
		jtf.setBackground(Color.LIGHT_GRAY);
		jtinv.setBackground(Color.LIGHT_GRAY);
		jtvec.setBackground(Color.LIGHT_GRAY);
		jPanel1.setBackground(Color.LIGHT_GRAY);
		jlcb.setEnabled(false);
		jlC.setEnabled(false);
		jlB.setEnabled(false);
		jlS.setEnabled(false);
	}

	void ev() {
		vs = new ArrayList<Character>();
		for (int i = 0; i < 8; i++) {
			vs.add((char) (i + '1'));
		}
	}

	int drwFnc(int[] f, Graphics2D g, double x, double y, double si, int mx, int my, float w, Color b) {
		s = new StringBuilder();
		s.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n");
		s.append("<!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\""
						+ " \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">\n");

		s.append("<svg version=\"1.1\"\nxmlns=\"http://www.w3.org/2000/svg\" "
						+ "xmlns:xlink=\"http://www.w3.org/1999/xlink\"\n");
		s.append("width=\"" + 200 + "\" ");
		s.append("height=\"" + 200 + "\" ");
		s.append(">\n");
		s.append("<g transform=\"translate(5,195)\">\n");

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setStroke(new BasicStroke(1f));

		AffineTransform sg = g.getTransform();
		g.scale(si / 200, si / 200);
		g.translate(x + 10, y + 180);

		int d = f[f.length - 1];
		if (d > 6) {
			return -1;
		}
		int c = drwCube(d, g, mx, my, si, x, y, w, b);

		for (int a = 0; a < f.length - 1; a++) {
			g.setColor(gr);
			p.rset();
			for (int i = 0; i < d; i++) {
				if ((f[a] >> i) % 2 == 1) {
					p._add(ces[i]);
				}
			}
			for (int j = a + 1; j < f.length - 1; j++) {
				if (nigh(f[a], f[j])) {
					q.rset();
					for (int k = 0; k < d; k++) {
						if ((f[j] >> k) % 2 == 1) {
							q._add(ces[k]);
						}
					}
					if (mx == 0) {
						g.draw(new Line2D.Double(p.x, p.y, q.x, q.y));
					}
					else {
						ef("line", "x1", p.x, "y1", p.y, "x2", q.x, "y2", q.y, "stroke-width", gw, "stroke", "black");
					}
				}
			}
		}
		for (int a = 0; a < f.length - 1; a++) {
			g.setColor(gr);
			p.rset();
			for (int i = 0; i < d; i++) {
				if ((f[a] >> i) % 2 == 1) {
					p._add(ces[i]);
				}
			}
			if (mx == 0) {
				g.setColor(w > 0 ? Color.WHITE : b);
				g.fill(new Ellipse2D.Double(p.x - pw / 2, p.y - pw / 2, pw, pw));
				g.setColor(gr);
				g.draw(new Ellipse2D.Double(p.x - pw / 2, p.y - pw / 2, pw, pw));
			}
			else {
				ef("circle", "cx", p.x, "cy", p.y, "r", pg);
			}
		}
		int fs = w > 0 ? 16 : 14;
		Font ff = new Font("Sans Serif", 0, fs);
		g.setFont(ff);
		g.setColor(Color.BLUE);
		q.rset();
		if (mx == 0) {
			g.fill(new Ellipse2D.Double(q.x - pww / 2, q.y - pww / 2, pww, pww));
		}
		for (int i = 0; i < d; i++) {
			q.rset();
			q._add(ces[i].mul(1.1));
			String n = vs.get(i) + "";
			Rectangle2D r = ff.getStringBounds(n, g.getFontRenderContext());
			double a = atan(q.y / q.x);
			q._add(ces[i].dir(fs * 0.5));
			g.setColor(Color.BLUE);
			if (mx == 0) {
				g.drawString(n, (float) (q.x + r.getCenterX() * sin(a)), (float) (q.y - r.getCenterY() * 0.9f * cos(a)));
			}
			q.rset();
			q._add(ces[i]);
			if (mx == 0) {
				g.fill(new Ellipse2D.Double(q.x - pww / 2, q.y - pww / 2, pww, pww));
			}
		}

		g.setTransform(sg);
		s.append("</g>\n");
		s.append("</svg>\n");
		return c;
	}

	int drwCube(int d, Graphics2D g, int mx, int my, double s, double x, double y, float w, Color b) {

		int c = -1;
		double md = 10;

		g.setStroke(new BasicStroke(w > 0 ? w : 1));
		for (int i = 0; i < e2(d); i++) {
			p.rset();
			for (int k = 0; k < d; k++) {
				if ((i >> k) % 2 == 1) {
					p._add(ces[k]);
				}
			}
			double dd = sqrt(p2((p.x + x + 10) * s / 200 - mx) + p2((p.y + y + 180) * s / 200 - my));
			if (dd < md) {
				md = dd;
				c = i;
			}
			for (int j = i + 1; j < e2(d); j++) {
				if (nigh(i, j)) {
					q.rset();
					for (int k = 0; k < d; k++) {
						if ((j >> k) % 2 == 1) {
							q._add(ces[k]);
						}
					}
					g.setColor(gra);
					if (mx == 0) {
						g.draw(new Line2D.Double(p.x, p.y, q.x, q.y));
					}
					else {
						ef("line", "x1", p.x, "y1", p.y, "x2", q.x, "y2", q.y, "stroke-width", gww, "stroke", "rgb(170,170,170)");
					}
				}
			}
		}
		for (int i = 0; i < e2(d); i++) {
			p.rset();
			for (int k = 0; k < d; k++) {
				if ((i >> k) % 2 == 1) {
					p._add(ces[k]);
				}
			}
			double dd = sqrt(p2((p.x + x + 10) * s / 200 - mx) + p2((p.y + y + 180) * s / 200 - my));
			if (dd < md) {
				md = dd;
				c = i;
			}
			if (mx == 0) {
				g.setColor(w > 0 ? Color.WHITE : b);
				g.fill(new Ellipse2D.Double(p.x - pw / 2, p.y - pw / 2, pw, pw));
				g.setColor(gra);
				g.draw(new Ellipse2D.Double(p.x - pw / 2, p.y - pw / 2, pw, pw));
			}
		}
		return c;
	}

	void ef(String n, Object... a) {
		s.append("<" + n + " ");
		for (int i = 0; i < a.length; i += 2) {
			s.append(a[i] + "=\"" + a[i + 1] + "\" ");
		}
		s.append("/>\n");
	}

	boolean nigh(int x, int y) {
		int d = x ^ y;
		if (d == 0) {
			return false;
		}
		int n = 0;
		for (int i = 0; i < 32; i++) {
			if ((d >> i) % 2 == 1) {
				n++;
			}
			if (n > 1) {
				return false;
			}
		}
		return true;
	}

	static class P2 {

		double x, y;

		P2(double x, double y) {
			this.x = x;
			this.y = y;
		}

		P2() {
		}

		P2 dir(double s) {
			double l = sqrt(p2(x) + p2(y));
			return new P2(x / l * s, y / l * s);
		}

		P2 mul(double s) {
			return new P2(x * s, y * s);
		}

		P2 _add(P2 p) {
			x += p.x;
			y += p.y;
			return this;
		}

		void rset() {
			x = 0;
			y = 0;
		}
	}

	static class ToolHelp extends javax.swing.JFrame {

		public ToolHelp() {
			initComponents();

			InputStream is = getClass().getResourceAsStream("resources/help.html");
			java.util.Scanner sc = new java.util.Scanner(is).useDelimiter("\\A");
			String s = sc.hasNext() ? sc.next() : "";
			jEditorPane1.setContentType("text/html");
			jEditorPane1.setText(s);
		}

  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    jScrollPane1 = new javax.swing.JScrollPane();
    jEditorPane1 = new javax.swing.JEditorPane();

    setTitle("SatConn Help");

    jEditorPane1.setEditable(false);
    jEditorPane1.setMargin(new java.awt.Insets(3, 10, 3, 3));
    jScrollPane1.setViewportView(jEditorPane1);

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 633, Short.MAX_VALUE)
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

		/**
		 * @param args the command line arguments
		 */
		static void main(String args[]) {
			/* Set the Nimbus look and feel */
			//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
			 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
			 */
			try {
				for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
					if ("Nimbus".equals(info.getName())) {
						javax.swing.UIManager.setLookAndFeel(info.getClassName());
						break;
					}
				}
			} catch (ClassNotFoundException ex) {
				java.util.logging.Logger.getLogger(ToolHelp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			} catch (InstantiationException ex) {
				java.util.logging.Logger.getLogger(ToolHelp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			} catch (IllegalAccessException ex) {
				java.util.logging.Logger.getLogger(ToolHelp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			} catch (javax.swing.UnsupportedLookAndFeelException ex) {
				java.util.logging.Logger.getLogger(ToolHelp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
			}
			//</editor-fold>

			/* Create and display the form */
			java.awt.EventQueue.invokeLater(new Runnable() {
				public void run() {
					new ToolHelp().setVisible(true);
				}
			});
		}

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JEditorPane jEditorPane1;
  private javax.swing.JScrollPane jScrollPane1;
  // End of variables declaration//GEN-END:variables
}
}

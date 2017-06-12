package com.google.security.zynamics.bindiff.graph.realizers;

import com.google.security.zynamics.bindiff.enums.ESide;
import com.google.security.zynamics.bindiff.graph.nodes.CombinedDiffNode;
import com.google.security.zynamics.zylib.gui.zygraph.realizers.IZyNodeRealizerListener;
import com.google.security.zynamics.zylib.gui.zygraph.realizers.ZyLabelContent;
import com.google.security.zynamics.zylib.yfileswrap.gui.zygraph.realizers.ZyNodeRealizer;

import y.view.LineType;
import y.view.NodeRealizer;
import y.view.ShapeNodeRealizer;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class CombinedNodeRealizer extends ZyNodeRealizer<CombinedDiffNode> {
  private static final byte SHADOW_SIZE = 8;
  private static final Color SHADOW_COLOR = Color.GRAY;
  private static final int SEPARATOR_GAP = 12;

  private final ZyLabelContent leftContent;
  private final ZyLabelContent rightContent;
  private final Rectangle2D bounds = new Rectangle();
  private ESide activeContentSide;

  public CombinedNodeRealizer(final ZyLabelContent leftContent, final ZyLabelContent rightContent) {
    this.leftContent = leftContent;
    this.rightContent = rightContent;

    activeContentSide = leftContent != null ? ESide.PRIMARY : ESide.SECONDARY;

    setShapeType(ShapeNodeRealizer.ROUND_RECT);

    setLineType(LineType.LINE_2);

    setDropShadowOffsetX(SHADOW_SIZE);
    setDropShadowOffsetY(SHADOW_SIZE);

    setDropShadowColor(SHADOW_COLOR);

    setNodeSize();
  }

  private boolean isUnmatched() {
    return leftContent == null || rightContent == null;
  }

  private void setNodeSize() {
    if (isUnmatched()) {
      bounds.setRect(getNodeContent().getBounds());
    } else {
      final double x = leftContent.getBounds().getX();
      final double y = leftContent.getBounds().getY();
      final double h =
          Math.max(leftContent.getBounds().getHeight(), rightContent.getBounds().getHeight());
      final double w =
          leftContent.getBounds().getWidth() + SEPARATOR_GAP + rightContent.getBounds().getWidth();

      bounds.setRect(x, y, w, h);
    }

    setSize(bounds.getWidth(), bounds.getHeight());
  }

  @Override
  protected void paintShadow(final Graphics2D gfx) {
    if (!isSelected() && isDropShadowVisible()) {
      gfx.setColor(SHADOW_COLOR);

      setDropShadowOffsetX(SHADOW_SIZE);
      setDropShadowOffsetY(SHADOW_SIZE);

      gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f));

      super.paintShadow(gfx);

      gfx.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.f));
    }
  }

  @Override
  public void addListener(final IZyNodeRealizerListener<?> listener) {
    // Do nothing.
  }

  @Override
  public void removeListener(final IZyNodeRealizerListener<?> listener) {
    // Do nothing.
  }

  @Override
  public ZyLabelContent getNodeContent() {
    if (isUnmatched()) {
      return leftContent == null ? rightContent : leftContent;
    }

    return activeContentSide == ESide.PRIMARY ? leftContent : rightContent;
  }

  @Override
  public NodeRealizer getRealizer() {
    return this;
  }

  @Override
  public void paintHotSpots(final Graphics2D gfx) {
    return;
  }

  @Override
  public void paintText(final Graphics2D g) {
    if (leftContent != null && rightContent == null) {
      leftContent.draw(g, getX(), getY());
    } else if (leftContent == null && rightContent != null) {
      rightContent.draw(g, getX(), getY());
    } else {
      final double w = leftContent.getBounds().getWidth();
      final int x = (int) Math.round(getX() + w + SEPARATOR_GAP / 2 - 1);
      final int y1 = (int) Math.round(getY() + 1);
      final int y2 = (int) Math.round(getY() + bounds.getHeight() - 1);

      g.setStroke(new BasicStroke(1f));
      leftContent.draw(g, getX(), getY());
      rightContent.draw(g, getX() + SEPARATOR_GAP + w, getY());

      g.setColor(Color.BLACK);
      g.setStroke(new BasicStroke(2f));
      g.drawLine(x, y1, x, y2);
    }

    super.paintText(g);
  }

  @Override
  public void regenerate() {
    if (isUnmatched()) {
      super.regenerate();

      return;
    }

    final double widthOld =
        leftContent.getBounds().getWidth() + SEPARATOR_GAP + rightContent.getBounds().getWidth();
    final double heightOld =
        leftContent.getBounds().getHeight() + rightContent.getBounds().getHeight();

    getUpdater().generateContent(this, leftContent);
    getUpdater().generateContent(this, rightContent);

    setNodeSize();

    scalePortCoordinates(getNode(), widthOld, bounds.getWidth(), heightOld, bounds.getHeight());

    notifyHasRegenerated();

    repaint();
  }

  public void setActiveContent(final ESide side) {
    if (!isUnmatched()) {
      activeContentSide = side;
    }
  }

  @Override
  public void updateContentSelectionColor() {
    if (isUnmatched()) {
      super.updateContentSelectionColor();

      return;
    }
  }
}
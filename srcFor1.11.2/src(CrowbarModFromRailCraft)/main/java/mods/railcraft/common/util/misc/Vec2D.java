package mods.railcraft.common.util.misc;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class Vec2D extends Point2D.Double
{
  public static final float DEGREES_45 = 0.7853982F;
  public static final float DEGREES_90 = 1.570796F;
  public static final float DEGREES_135 = 2.356195F;
  public static final float DEGREES_180 = 3.141593F;
  public static final float DEGREES_270 = 4.712389F;

  public Vec2D()
  {
  }

  public Vec2D(Point2D p)
  {
    super(p.getX(), p.getY());
  }

  public Vec2D(double x, double y)
  {
    super(x, y);
  }

  public Vec2D clone()
  {
    return new Vec2D(this.x, this.y);
  }

  public static Vec2D fromPolar(double angle, float magnitude)
  {
    Vec2D v = new Vec2D();
    v.setFromPolar(angle, magnitude);
    return v;
  }

  public static Vec2D add(Point2D a, Point2D b)
  {
    return new Vec2D(a.getX() + b.getX(), a.getY() + b.getY());
  }

  public static Vec2D subtract(Point2D a, Point2D b)
  {
    return new Vec2D(a.getX() - b.getX(), a.getY() - b.getY());
  }

  public void setLocation(int x, int y)
  {
    this.x = x;
    this.y = y;
  }

  public void setFromPolar(double angle, float magnitude)
  {
    float x = (float)Math.cos(angle) * magnitude;
    float y = (float)Math.sin(angle) * magnitude;
    setLocation(x, y);
  }

  public void zero()
  {
    this.x = (this.y = 0.0D);
  }

  public void normalize()
  {
    double mag = magnitude();
    if (mag != 0.0D)
      setLocation(this.x / mag, this.y / mag);
  }

  public Vec2D unitVector()
  {
    Vec2D v = clone();
    v.normalize();
    return v;
  }

  public double magnitude()
  {
    return Math.sqrt(this.x * this.x + this.y * this.y);
  }

  public void setMagnitude(float mag)
  {
    setFromPolar(angle(), mag);
  }

  public double magnitudeSq()
  {
    return this.x * this.x + this.y * this.y;
  }

  public void negate()
  {
    this.x = (-this.x);
    this.y = (-this.y);
  }

  public float angle()
  {
    return (float)Math.atan2(this.y, this.x);
  }

  public void rotate(double theta)
  {
    double nx = this.x * (float)Math.cos(theta) - this.y * (float)Math.sin(theta);
    double ny = this.x * (float)Math.sin(theta) + this.y * (float)Math.cos(theta);
    setLocation(nx, ny);
  }

  public void rotate90()
  {
    double ox = this.x;
    this.x = (-this.y);
    this.y = ox;
  }

  public void rotate270()
  {
    double ox = this.x;
    this.x = this.y;
    this.y = (-ox);
  }

  public void subtract(Point2D p)
  {
    this.x -= p.getX();
    this.y -= p.getY();
  }

  public void subtract(int x, int y)
  {
    this.x -= x;
    this.y -= y;
  }

  public void subtract(double x, double y)
  {
    this.x -= (int)x;
    this.y -= (int)y;
  }

  public void add(Point2D p)
  {
    this.x += p.getX();
    this.y += p.getY();
  }

  public void add(int x, int y)
  {
    this.x += x;
    this.y += y;
  }

  public void add(double x, double y)
  {
    this.x += (int)x;
    this.y += (int)y;
  }

  public void transform(AffineTransform a)
  {
    setLocation(a.transform(this, null));
  }

  public Vec2D makeTransform(AffineTransform a)
  {
    return new Vec2D(a.transform(this, null));
  }

  public double dotProduct(Point2D v)
  {
    return this.x * v.getX() + this.y * v.getY();
  }

  public double angleBetween(Vec2D v)
  {
    double a = dotProduct(v);
    double magnitude = magnitude() * v.magnitude();
    if (magnitude == 0.0D) {
      return 0.0D;
    }
    a /= magnitude;

    if (a > 1.0D)
      a = 1.0D;
    else if (a < -1.0D) {
      a = -1.0D;
    }
    return Math.acos(a);
  }

  public double angleTo(Point2D a)
  {
    return Math.atan2(a.getY() - this.y, a.getX() - this.x);
  }

  public double angleFrom(Point2D a)
  {
    return Math.atan2(this.y - a.getY(), this.x - a.getX());
  }

  public void scale(float scale)
  {
    this.x *= scale;
    this.y *= scale;
  }

  public void addScale(float scale, Point2D v)
  {
    setLocation(this.x + v.getX() * scale, this.y + v.getY() * scale);
  }
}
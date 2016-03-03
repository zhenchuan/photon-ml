package com.linkedin.photon.ml.optimization

import com.linkedin.photon.ml.data.LabeledPoint
import com.linkedin.photon.ml.function.{TwiceDiffFunction, DiffFunction}
import org.apache.spark.Logging

/**
 * Creates instances of Optimizer according to the objective function type and configuration. The factory methods in
 * this class enforce the runtime rules for compatibility between user-selected loss functions and optimizers.
 */

protected[ml] object OptimizerFactory {

  /**
   * Creates an optimizer for DiffFunction objective functions.
   *
   * @param config Optimizer configuration
   * @return A new DiffFunction Optimizer created according to the configuration
   */
  def diffOptimizer(config: OptimizerConfig): Optimizer[LabeledPoint, DiffFunction[LabeledPoint]]  = {
    val optimizer = config.optimizerType match {
      case OptimizerType.LBFGS =>
        new LBFGS[LabeledPoint]
      case optType =>
        throw new IllegalArgumentException(s"Incompatible optimizer selected: $optType.");
    }

    optimizer.setMaximumIterations(config.maximumIterations)
    optimizer.setTolerance(config.tolerance)
    optimizer.setConstraintMap(config.constraintMap)

    optimizer
  }

  /**
   * Creates an optimizer for TwiceDiffFunction objective functions.
   *
   * @param config Optimizer configuration
   * @return A new TwiceDiffFunction Optimizer created according to the configuration
   */
  def twiceDiffOptimizer(config: OptimizerConfig): Optimizer[LabeledPoint, TwiceDiffFunction[LabeledPoint]] = {
    val optimizer = config.optimizerType match {
      case OptimizerType.LBFGS =>
        new LBFGS[LabeledPoint]
      case OptimizerType.TRON =>
        new TRON[LabeledPoint]
      case optType =>
        throw new IllegalArgumentException(s"Incompatible optimizer selected: $optType.");
    }

    optimizer.setMaximumIterations(config.maximumIterations)
    optimizer.setTolerance(config.tolerance)
    optimizer.setConstraintMap(config.constraintMap)

    optimizer
  }

}
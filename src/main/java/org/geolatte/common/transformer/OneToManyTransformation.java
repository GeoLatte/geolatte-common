package org.geolatte.common.transformer;

import java.util.Iterator;

/**
 * This interface is the basic representation of a transformation that takes a single input element and transforms it
 * into multiple output elements.
 *
 * @param <Source> The input type of this Transformation.
 * @param <Target> The output type of this Transformation.
 *
 * <p>
 * <i>Creation-Date</i>: 03/12/11<br>
 * <i>Creation-Time</i>:  16:18<br>
 * </p>
 *
 * @author Bert Vanhooff
 * @author <a href="http://www.qmino.com">Qmino bvba</a>
 * @since SDK1.5
 */
public interface OneToManyTransformation<Source, Target> extends Transformation<Source, Iterator<Target>> {
}

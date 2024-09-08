package org.jyj.common.validation;

import org.jyj.common.validation.group.LengthCheckGroup;
import org.jyj.common.validation.group.MinMaxGroup;
import org.jyj.common.validation.group.NotEmptyGroup;
import org.jyj.common.validation.group.PatternCheckGroup;

import javax.validation.GroupSequence;

@GroupSequence({NotEmptyGroup.class, LengthCheckGroup.class, MinMaxGroup.class, PatternCheckGroup.class})
public interface ValidationSequence {
}

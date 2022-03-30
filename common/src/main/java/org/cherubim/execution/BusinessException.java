package org.cherubim.execution;

import lombok.Getter;
import lombok.Setter;

/**
 * 业务异常基类
 *
 * @author panhong
 */
@Getter
@Setter
public abstract class BusinessException extends RuntimeException {

  /** 错误编码 */
  private String code;

  /** 错误信息 */
  private String message;
}

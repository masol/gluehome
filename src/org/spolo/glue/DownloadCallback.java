package org.spolo.glue;

import java.io.File;
import java.util.Collection;

public interface DownloadCallback {
  /**
   * 处理下载结果
   * @param result 下载完成后会将结果文件放入Collection<File>中
   * 		如果result为null，说明调用失败。
   */
  public void handleResult(Collection<File> result);
}

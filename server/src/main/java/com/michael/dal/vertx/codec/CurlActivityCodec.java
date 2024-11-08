package com.michael.dal.vertx.codec;

import com.michael.dal.utils.ObjectMapperProvider;
import com.michael.dal.vertx.models.CurlActivity;
import io.vertx.core.eventbus.MessageCodec;
import java.io.IOException;

public class CurlActivityCodec implements MessageCodec<CurlActivity, CurlActivity> {
  @Override
  public void encodeToWire(io.vertx.core.buffer.Buffer buffer, CurlActivity curlActivity) {
    byte[] data = serialize(curlActivity);
    buffer.appendInt(data.length);
    buffer.appendBytes(data);
  }

  @Override
  public CurlActivity decodeFromWire(int pos, io.vertx.core.buffer.Buffer buffer) {
    int length = buffer.getInt(pos);
    byte[] data = buffer.getBytes(pos + 4, pos + 4 + length);
    return deserialize(data);
  }

  @Override
  public CurlActivity transform(CurlActivity curlActivity) {
    return curlActivity;
  }

  @Override
  public String name() {
    return "curl-activity-codec";
  }

  @Override
  public byte systemCodecID() {
    return -1;
  }

  private byte[] serialize(CurlActivity curlActivity) {
    try {
      return ObjectMapperProvider.getInstance().writeValueAsBytes(curlActivity);
    } catch (IOException e) {
      throw new RuntimeException("Failed to serialize CurlActivity", e);
    }
  }

  private CurlActivity deserialize(byte[] data) {
    try {
      return ObjectMapperProvider.getInstance().readValue(data, CurlActivity.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to deserialize CurlActivity", e);
    }
  }
}

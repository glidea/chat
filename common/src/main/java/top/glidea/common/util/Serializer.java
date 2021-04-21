package top.glidea.common.util;

import com.google.gson.Gson;
import io.protostuff.LinkedBuffer;
import io.protostuff.ProtobufIOUtil;
import io.protostuff.runtime.RuntimeSchema;

public enum Serializer {
    /**
     * Protobuf高性能序列化（binary）
     */
    Protobuf {
        @Override
        public <T> byte[] serialize(T t, Class<T> classOfT) {
            return ProtobufIOUtil.toByteArray(t, RuntimeSchema.createFrom(classOfT),
                    LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
        }

        @Override
        public <T> T deSerialize(byte[] bytes, Class<T> classOfT) {
            RuntimeSchema<T> runtimeSchema = RuntimeSchema.createFrom(classOfT);
            T t = runtimeSchema.newMessage();
            ProtobufIOUtil.mergeFrom(bytes, t, runtimeSchema);
            return t;
        }

        /**
         * Protobuf序列化，请用serialize
         */
        @Deprecated
        @Override
        public String toJsonString(Object o) {
            return super.toJsonString(o);
        }
        /**
         * Protobuf序列化，请用deserialize
         */
        @Deprecated
        @Override
        public <T> T toObj(String json, Class<T> classOfT) {
            return super.toObj(json, classOfT);
        }
    },
    /**
     * Json序列化（text），Gson实现
     */
    Json {
        private final Gson GSON = new Gson();

        @Override
        public String toJsonString(Object o) {
            return GSON.toJson(o);
        }

        @Override
        public <T> T toObj(String json, Class<T> classOfT) {
            return GSON.fromJson(json, classOfT);
        }

        /**
         * Json序列化请用 toJsonString
         */
        @Deprecated
        @Override
        public <T> byte[] serialize(T t, Class<T> classOfT) {
            return super.serialize(t, classOfT);
        }
        /**
         * Json反序列化请用 toObj
         */
        @Deprecated
        @Override
        public <T> T deSerialize(byte[] bytes, Class<T> classOfT) {
            return super.deSerialize(bytes, classOfT);
        }
    };

    public <T> byte[] serialize(T t, Class<T> classOfT) {
        throw new UnsupportedOperationException();
    }

    public <T> T deSerialize(byte[] bytes, Class<T> classOfT) {
        throw new UnsupportedOperationException();
    }

    public String toJsonString(Object o) {
        throw new UnsupportedOperationException();
    }

    public <T> T toObj(String json, Class<T> classOfT) {
        throw new UnsupportedOperationException();
    }
}

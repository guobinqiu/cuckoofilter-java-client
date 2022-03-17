# Cuckoofilter gRPC Java Client

This demo was written based on [JDK17](https://code.visualstudio.com/docs/java/java-project#_download-jdks).

To run the client, you should have the [cuckoofilter server](https://github.com/guobinqiu/cuckoofilter) started first.

### Prerequisites

1. Install [Gradle](https://gradle.org/install/).

2. Install [Visual Studio Code](https://code.visualstudio.com/download).

3. Configure the JDK in vscode as described [here](https://code.visualstudio.com/docs/java/java-tutorial).

4. Add the following vscode extensions if you still don’t have them:

- Language Support for Java(TM) by Red Hat
- Gradle Language Support
- vscode-proto3

### Update your own `build.gradle`

For operation system other than Mac, you should remove the trailing `:osx-x86_64`.

For `io.grpc:protoc-gen-grpc-java:1.43.0`, just for my osx is `10.13`, you'd better use the latest version instead.

```
protobuf {
  protoc {
    artifact = "com.google.protobuf:protoc:3.19.2:osx-x86_64"
  }
  plugins {
    grpc {
      artifact = 'io.grpc:protoc-gen-grpc-java:1.43.0:osx-x86_64'
    }
  }
  generateProtoTasks {
    all()*.plugins {
      grpc {}
    }
  }
}
```

### Run the client

```
gradle run
```

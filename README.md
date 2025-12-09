# Actionbase

Actionbase is a high-performance graph database for processing large-scale user activity data. It is production-proven, serving tens of millions of users in [Kakao](/about-kakao).

## What is Actionbase?

Actionbase is an OLTP (Online Transaction Processing) system that uses a graph data model, specifically designed for processing large-scale user activity data. It efficiently stores and queries user activity data at scale, providing a generalized interface built on top of storage backends like HBase. Actionbase provides REST API for easy integration with your applications.

### Project Goals

- **Real-time Serving**: Low-latency responses under 10ms for read traffic
- **Large-scale Traffic Processing**: Handling hundreds of thousands of requests per second (RPS)
- **Massive Data Management**: Storage and management of millions of TB of data
- **Flexible Scalability**: Responding to service growth through horizontal scaling

### Key Features

- **Write-time Optimization**: Pre-computes indexes and counters during writes for ultra-fast reads
- **Property Graph Model**: Supports standard property graph data model with flexible schema
- **Easy Integration**: REST API and client libraries for seamless integration with your applications
- **Lambda Architecture Support**: Provides bulk loading, asynchronous processing, and data pipeline capabilities

## Architecture

Actionbase is built with a modular architecture:

- **core** (codec-java, core-java): Core data model definition and data processing logic
  - Java, Kotlin (Java 8 compatible) for compatibility (e.g., pipeline)
  - Data encoding/decoding for physical storage
  - Event and state change processing
  
- **engine**: Business logic engine
  - Kotlin
  - Pure business logic independent of transport protocols
  - HBase communication, metadata management, data mutation, and query execution

- **server**: High-performance REST API server
  - Kotlin, Spring WebFlux
  - Asynchronous API processing

- **pipeline**: Data processing *(Coming soon)*
  - Scala (Java 8), Apache Spark
  - Async Processing, Bulk loading, backup, and real-time ETL

### Datastore

Actionbase currently uses HBase as its storage layer, chosen for its reliability and scalability. Future releases will support additional storage backends including SlateDB.

## Production Usage

Actionbase powers several major [Kakao](/about-kakao) services including KakaoTalk and KakaoShopping, serving tens of millions of users with real-time activity data processing.

**Production Performance:**
- Read latency: <5ms (p99)
- Data scale: Multiple TB-scale deployments

## Learn More

- [Documentation](https://actionbase.dev/)
- [Introduction to Actionbase (Korean) / if(kakaoAI)2024](https://www.youtube.com/watch?v=8-hVAFVHISE)

## Contributing

We welcome contributions! Please see our contributing guidelines for details on:

- Code style and conventions
- Submitting issues and pull requests
- Development workflow

For more information, please visit our [Community](https://actionbase.dev/community/github/) page.

## Current Status

This project is in the initial open source preparation phase. Internal components will be released progressively.

## License

This software is licensed under the [Apache 2 license](LICENSE).

Copyright 2026 Kakao Corp. <http://www.kakaocorp.com>

Licensed under the Apache License, Version 2.0 (the "License"); you may not
use this project except in compliance with the License. You may obtain a copy
of the License at http://www.apache.org/licenses/LICENSE-2.0.

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.

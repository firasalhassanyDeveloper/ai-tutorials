# ai-tutorials

This project contains multiple modules, each designed to explore and demonstrate different experiments in AI development.

### spring-ai-model-running
This module demonstrates how to integrate a basic Spring AI application with a locally runn

### prompt-templates
is a dedicated part of your application that stores, builds, and manages prompts used for LLM (AI) requests.

### chat-storage
This module shows how user questions are stored in a PostgreSQL database to enable chat memory. The database is automatically started using Docker Compose. After running the application at http://localhost:8080, you can use the chat interface and see that the model remembers previous messages within the same session, identified by the HTTP session ID.
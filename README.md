# Almazon: Client Side

## Requirements:

1. The Glassfish server **MUST** be running on port 8080.

2. The Glassfish server **MUST** have the JSON exchange enabled and working to login, as the ```getPublicKey``` request only works with JSON. If GlassFish misses this config, you still will be able to login, but the **public key will be retrieved locally.**

### Configure server for JSON responses:

1. Download this jar from [org.eclipse.persistence.moxy.jar - Google Drive](https://drive.google.com/file/d/1-ubLLgVRjagJGzApejbumyQz-ZgQld1U/view?usp=sharing)

2. Save and replace this file into ```[YOUR__GLASSFISH__SERVER__DIRECTORY]/glassfish/modules```

3. You're now able to get JSON responses.

## Login credentials:

| USER   | PASSWORD       | PRIVILEGE              | NOTES                                                        |
| ------ | -------------- | ---------------------- | ------------------------------------------------------------ |
| Mikel  | ```1234$%Mm``` | <mark>SUPERUSER</mark> | This is the only superuser that can manage other superusers. |
| Aketza | ```1234$%Mm``` | WORKER                 |                                                              |
| Iker   | ```1234$%Mm``` | PROVIDER               |                                                              |
| Imanol | ```1234$%Mm``` | SUPERUSER              |                                                              |
| Asier  | ```1234$%Mm``` | SUPERUSER              |                                                              |

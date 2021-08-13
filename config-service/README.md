## Config Server Application
It is a server which bring the service-config files from the repository.
### Path
```text
localhost:{<strong>port</strong>}/{<strong>name</strong>}/{<strong>profile</strong>}/<strong>label</strong>
```

Environment:
 - name: name of the configuration file/service
 - profile: If is default, or dev, or some profile configuration
 - label: Name of the branch

#### Example:
```text
localhost:8080/some-service/default/main
```
This request will response the some-service.yml configuration file from the
repository at the main branch

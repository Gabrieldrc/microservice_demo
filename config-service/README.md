<h2>Config Server Application</h2>
<p>It is a server which bring the service-config files from the repository.</p>
<h3>Path</h3>
```text
localhost:{<strong>port</strong>}/{<strong>name</strong>}/{<strong>profile</strong>}/<strong>label</strong>
```

<ul>
Environment:
<li>name: name of the configuration file/service</li>
<li>profile: If is default, or dev, or some profile configuration</li>
<li>label: Name of the branch</li>
</ul>

<h4>Example:</h4>
```text
localhost:8080/some-service/default/main
```
<p>
This request will response the some-service.yml configuration file from the
repository at the main branch
</p>
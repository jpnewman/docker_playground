# NOTE: This makefile is designed to only work with single services.

.PHONY: all clean init up down logs exec exec_root info

STACK_NAME = gerrit

all: clean init up

clean: down
	rm -rf filesystems

init:
	docker swarm init > /dev/null || true

	mkdir -p filesystems/ldap
	mkdir -p filesystems/postgres

up:
	docker stack deploy --compose-file stack.yml $(STACK_NAME)

down: clean
	docker stack rm $(STACK_NAME) || true

logs:
    ifdef SERVICE
			docker service logs $(STACK_NAME)_$(SERVICE)
    else
			@echo "Run with SERVICE argument: -"
			@echo "e.g."
			@echo ""
			@echo "  make logs SERVICE=ldap"
			@echo ""
    endif

exec:
    ifdef SERVICE
			docker exec -it $(STACK_NAME)_$(SERVICE).1.`docker service ps -f 'name=$(STACK_NAME)_$(SERVICE).1' $(STACK_NAME)_$(SERVICE) -q --no-trunc | head -n1` /bin/bash
    else
			@echo "Run with SERVICE argument: -"
			@echo "e.g."
			@echo ""
			@echo "  make exec SERVICE=gerrit"
			@echo ""
    endif

exec_root:
    ifdef SERVICE
			docker exec -u 0 -it $(STACK_NAME)_$(SERVICE).1.`docker service ps -f 'name=$(STACK_NAME)_$(SERVICE).1' $(STACK_NAME)_$(SERVICE) -q --no-trunc | head -n1` /bin/bash
    else
			@echo "Run with SERVICE argument: -"
			@echo "e.g."
			@echo ""
			@echo "  make exec_root SERVICE=gerrit"
			@echo ""
    endif

info:
	docker stack services $(STACK_NAME)

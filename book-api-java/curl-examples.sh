#!/usr/bin/env sh
set -eu
BASE_URL="${BASE_URL:-http://localhost:8080}"

echo "Create"
CREATED="$(curl -fsS -X POST "$BASE_URL/books" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Война и мир","author":"Лев Толстой","isbn":"978-5-389-01234-5","year":1869,"genre":"Роман","available":true}')"
echo "$CREATED"
ID="$(printf '%s' "$CREATED" | sed -n 's/.*"id":[[:space:]]*\([0-9][0-9]*\).*/\1/p')"
if [ -z "$ID" ]; then echo "Could not parse book id"; exit 1; fi

echo "Read all"
curl -fsS "$BASE_URL/books"; echo
echo "Read one"
curl -fsS "$BASE_URL/books/$ID"; echo
echo "Update"
curl -fsS -X PUT "$BASE_URL/books/$ID" \
  -H 'Content-Type: application/json' \
  -d '{"title":"Война и мир","author":"Лев Толстой","isbn":"978-5-389-01234-5","year":1869,"genre":"Эпический роман","available":false}'; echo
echo "Delete"
curl -fsS -X DELETE "$BASE_URL/books/$ID" -o /dev/null -w 'HTTP %{http_code}\n'
echo "Done"
